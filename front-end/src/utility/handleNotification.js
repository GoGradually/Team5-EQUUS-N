export const filterNotifications = (notifications) => {
  const filteredNotis = notifications.filter((notification) => {
    if (notification.read) return false; // 읽은 알람이면 무시
    if (notification.type === 'feedbackReceive') return true;
    if (notification.type === 'feedbackReportCreate') return true;
    if (notification.type === 'frequentFeedbackRequest') return true;
    if (notification.type === 'unreadFeedbackExist') return true;
    return false;
  });

  const typesSet = new Set();

  /** @type {Banner[]} */
  const banners = [];

  filteredNotis.forEach((notification) => {
    if (!typesSet.has(notification.type)) {
      typesSet.add(notification.type);
      banners.push({ notification, ids: [notification.notificationId] });
    } else {
      banners
        .find((banner) => banner.notification.type === notification.type)
        .ids.push(notification.notificationId);
    }
  });

  return banners;
};
