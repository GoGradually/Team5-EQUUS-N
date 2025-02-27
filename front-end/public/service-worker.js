import { cleanupOutdatedCaches, precacheAndRoute } from 'workbox-precaching';

cleanupOutdatedCaches();
// self.__WB_MANIFEST는 프리캐시할 파일 목록을 자동으로 생성합니다.
precacheAndRoute(self.__WB_MANIFEST);

// 푸시 알림 관련
self.addEventListener('push', function (event) {
  // 푸시 알림이 도착할 때 발생하는 push 이벤트를 리스닝하고, 해당 알림을 처리합니다.
  let notification = '알림 없음';
  try {
    notification = event.data.json();
  } catch (error) {
    console.error('Invalid JSON data:', error);
  }
  const notificationType = notification.type;

  let clickUrl = '';
  let notificationBody = '';
  let parameter = {};
  switch (notificationType) {
    case 'feedbackReceive':
      clickUrl = `/feedback/received`;
      notificationBody = '피드백이 도착했어요!';
      break;
    case 'heartReaction':
      clickUrl = `/feedback/sent`;
      notificationBody = '피드백이 도움이 됐어요!';
      break;
    case 'frequentFeedbackRequest':
      clickUrl = `/feedback/send/frequent`;
      parameter = {
        teamId: notification.teamId,
        memberId: notification.senderId,
        isRegular: false,
      };
      notificationBody = '피드백을 요청받았어요! 작성하러 가볼까요?';
      break;
    case 'feedbackReportCreate':
      clickUrl = `/mypage/report`;
      notificationBody = '피드백 리포트가 생성됐어요! 보러 가볼까요?';
      break;
    case 'unreadFeedbackExist':
      clickUrl = `/feedback/received`;
      notificationBody = '아직 읽지 않은 피드백이 있어요!';
      break;
    case 'teamLeaderChange':
      clickUrl = `/`;
      notificationBody = '팀장이 되었어요! 팀을 이끌 준비가 되셨나요?';
      break;
    case 'scheduleCreate':
      clickUrl = `/calendar`;
      parameter = {
        scheduleDate: notification.scheduleDate,
      };
      notificationBody = '새로운 일정이 추가되었어요!';
      break;
    case 'regularFeedbackRequest':
      clickUrl = `/feedback/send`;
      parameter = {
        scheduleId: notification.scheduleId,
        isRegular: true,
      };
      notificationBody = '일정이 끝났으니 피드백을 작성해볼까요?';
      break;
  }

  const options = {
    body: notificationBody,
    icon: '/logo.png',
    data: { clickUrl, parameter },
  };
  event.waitUntil(
    Promise.allSettled([
      sendMessageToClients(notificationType),
      // 푸시 알림을 정상적으로 브라우저에 표시할 때까지 작업이 중단되지 않도록 보장하는 역할
      self.registration.showNotification('피드한줌', options), // Service Worker의 registration 객체에서 showNotification() 메서드를 호출하여 푸시 알림을 실제로 표시합니다.
    ]),
  );
});

// 푸시 알림을 클릭했을 때 발생하는 notificationclick 이벤트를 리스닝하고, 해당 알림을 처리합니다.
self.addEventListener('notificationclick', function (event) {
  event.preventDefault();
  const notification = event.notification;
  let queryParameter = `?redirect=${encodeURIComponent(notification.data.clickUrl)}`;
  const params = event.notification.data.parameter;
  Object.keys(params).forEach((key) => {
    const value = params[key];
    queryParameter += `&${encodeURIComponent(key)}=${encodeURIComponent(value)}`;
  });

  const url = new URL(queryParameter, self.location.origin + '/main').href;
  notification.close();
  event.waitUntil(self.clients.openWindow(url));
});

async function sendMessageToClients(type) {
  const clients = await self.clients.matchAll({
    type: 'window',
    includeUncontrolled: true,
  });
  const data = { type };
  for (const client of clients) {
    client.postMessage(data);
  }
}
