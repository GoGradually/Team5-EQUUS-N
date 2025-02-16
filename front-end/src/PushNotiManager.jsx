import { useEffect } from 'react';
import { useAppServerKey, useSubscribe } from './api/usePushNoti';
import { useUser } from './useUser';

export default function PushNotiManager() {
  const { data } = useAppServerKey();
  const { mutate: subscribe } = useSubscribe();
  const { userId } = useUser();

  useEffect(() => {
    if (!data) return;
    const applicationServerKey = data.applicationServerKey;

    // 서비스 워커 등록
    if ('serviceWorker' in navigator) {
      navigator.serviceWorker
        .getRegistration('/service-worker.js')
        .then((registration) => {
          if (!registration) {
            navigator.serviceWorker
              .register('/service-worker.js')
              .then(function (registration) {
                console.log('Service Worker 등록 성공:', registration);
              })
              .catch(function (error) {
                console.error('Service Worker 등록 실패:', error);
              });
          }
        });
    } else {
      console.log(
        'Service Worker를 지원하지 않는 브라우저입니다. 푸시 알림을 사용할 수 없습니다.',
      );
    }

    // 알림 권한 요청 및 구독 설정
    if ('Notification' in window) {
      askForNotificationPermission();
    } else {
      console.log(
        'Notification을 지원하지 않는 브라우저입니다. 알림을 사용할 수 없습니다.',
      );
    }

    function askForNotificationPermission() {
      Notification.requestPermission().then((permission) => {
        if (permission !== 'granted') {
          console.error('알림 권한이 거부되었습니다.');
          return;
        }
        console.log('알림이 허용되었습니다');
        // 알림 권한이 허용되었을 때 구독 요청
        useConfigurePushSubscription();
      });
    }

    async function useConfigurePushSubscription() {
      if (!('serviceWorker' in navigator)) {
        return;
      }
      let serviceWorkerRegistration = await navigator.serviceWorker.ready;
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
  }, [data, subscribe, userId]);
}
