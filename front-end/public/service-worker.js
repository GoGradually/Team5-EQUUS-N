import { precacheAndRoute } from 'workbox-precaching';

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
  const receiverId = notification.receiverId;

  let clickUrl = '';
  let notificationBody = '';
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
      clickUrl = `/feedback/send`;
      notificationBody = '피드백을 요청받았어요!';
      break;
    case 'feedbackReportCreate':
      clickUrl = `/mypage/report`;
      notificationBody = '피드백 리포트가 생성됐어요! 보러가볼까요?';
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
      clickUrl = `/`;
      notificationBody = '새로운 일정이 추가되었어요!';
      break;
    case 'regularFeedbackRequest':
      clickUrl = `/feedback/send`;
      notificationBody = '일정이 끝났으니 피드백을 작성해볼까요?';
      break;
  }

  const options = {
    body: notificationBody,
    icon: '/logo.png',
    data: { clickUrl },
  };
  console.log('serviceWorker.push: ', notification);
  event.waitUntil(
    // 푸시 알림을 정상적으로 브라우저에 표시할 때까지 작업이 중단되지 않도록 보장하는 역할
    self.registration.showNotification('피드한줌', options), // Service Worker의 registration 객체에서 showNotification() 메서드를 호출하여 푸시 알림을 실제로 표시합니다.
  );
});

// 푸시 알림을 클릭했을 때 발생하는 notificationclick 이벤트를 리스닝하고, 해당 알림을 처리합니다.
self.addEventListener('notificationclick', function (event) {
  event.preventDefault();
  const notification = event.notification;
  const url = new URL(notification.data.clickUrl, self.location.origin).href;
  notification.close();

  event.waitUntil(self.clients.openWindow(url));
});

// 푸시 알림이 닫힐 때 발생하는 notificationclose 이벤트를 리스닝하고, 해당 알림을 처리합니다.
self.addEventListener('notificationclose', function (event) {
  console.log('Notification was closed', event);
});
