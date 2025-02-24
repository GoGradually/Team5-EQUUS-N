import { useMutation, useQuery } from '@tanstack/react-query';
import { api } from './baseApi';
import { useUser } from '../store/useUser';
import { useCallback } from 'react';
import { registerSW } from 'virtual:pwa-register';

const useSubscribe = () => {
  return useMutation({
    mutationFn: (subscription) =>
      api.post({
        url: '/api/push-notification/subscribe',
        body: subscription,
      }),
  });
};

const useAppServerKey = () => {
  return useQuery({
    queryKey: ['app-server-key'],
    queryFn: () =>
      api.get({ url: '/api/push-notification/application-server-key' }),
  });
};

function urlBase64ToUint8Array(base64String) {
  const padding = '='.repeat((4 - (base64String.length % 4)) % 4);
  const base64 = (base64String + padding)
    .replace(/\-/g, '+')
    .replace(/_/g, '/');
  const rawData = window.atob(base64);
  const outputArray = new Uint8Array(rawData.length);
  for (let i = 0; i < rawData.length; ++i) {
    outputArray[i] = rawData.charCodeAt(i);
  }
  return outputArray;
}

export default function usePushNoti() {
  const { data, isLoading } = useAppServerKey();
  const { mutate: subscribe } = useSubscribe();
  const { userId } = useUser();

  const setPushNoti = useCallback(
    async (_) => {
      if (!data) return;
      const applicationServerKey = data.applicationServerKey;
      // 알림 권한 요청 및 구독 설정
      if ('Notification' in window) {
        await askForNotificationPermission();
      } else {
        console.log(
          'Notification을 지원하지 않는 브라우저입니다. 알림을 사용할 수 없습니다.',
        );
      }

      async function askForNotificationPermission() {
        const permission = await Notification.requestPermission();
        if (permission !== 'granted') {
          console.error('알림 권한이 거부되었습니다.');
          return;
        }
        console.log('알림이 허용되었습니다');
        // 알림 권한이 허용되었을 때 구독 요청
        await useConfigurePushSubscription();
      }

      async function useConfigurePushSubscription() {
        if (!('serviceWorker' in navigator)) {
          return;
        }
        const serviceWorkerRegistration = await navigator.serviceWorker.ready;
        serviceWorkerRegistration.update();
        let subscription =
          await serviceWorkerRegistration.pushManager.getSubscription();
        if (subscription === null) {
          subscription = await serviceWorkerRegistration.pushManager.subscribe({
            userVisibleOnly: true,
            applicationServerKey: urlBase64ToUint8Array(applicationServerKey),
          });
        }
        subscribe({ subscription: subscription.toJSON() });
      }
    },
    [data, subscribe, userId],
  );

  return { setPushNoti, isLoading };
}

export function stopPush() {
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.ready.then((registration) => {
      registration.pushManager.getSubscription().then((subscription) => {
        if (subscription) {
          subscription.unsubscribe();
        }
      });
    });
  }
}
