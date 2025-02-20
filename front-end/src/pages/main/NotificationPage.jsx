import { useEffect, useState } from 'react';
import { useNotification } from '../../api/useMainPage';
import NavBar2 from '../../components/NavBar2';
import StickyWrapper from '../../components/wrappers/StickyWrapper';
import Alarm from './components/Alarm';
import { useNavigate } from 'react-router-dom';
export default function NotificationPage() {
  const [selectedTeamId, setSelectedTeamId] = useState(1);
  const { data: notificationsData, markAsRead } =
    useNotification(selectedTeamId);
  const [notifications, setNotifications] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (notificationsData) {
      console.log(notificationsData);
      setNotifications(notificationsData);
    }
  }, [notificationsData]);

  return (
    <div className='flex h-full flex-col'>
      <StickyWrapper>
        <NavBar2
          isCloseLeft={true}
          canClose={true}
          title='알림함'
          onClickClose={() => {
            navigate('/main');
          }}
        />
        <div className='border-b border-gray-700'></div>
      </StickyWrapper>
      <h2 className='subtitle-1 text-gray-0 mt-6 mb-2'>새로운 알림</h2>
      <ul className='h-full'>
        {notifications.length > 0 ?
          notifications.map((notification) => (
            <li
              key={notification.notificationId}
              onClick={() =>
                markAsRead({ notificationIds: [notification.notificationId] })
              }
            >
              <Alarm type={notification.type} data={notification} />
            </li>
          ))
        : <div className='flex h-full flex-col items-center justify-center gap-4 text-gray-300'>
            <p className='text-5xl'>📭</p>
            <p>받은 알림이 없어요</p>
          </div>
        }
      </ul>
    </div>
  );
}
