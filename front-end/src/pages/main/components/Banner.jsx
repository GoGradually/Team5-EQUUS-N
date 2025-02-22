import classNames from 'classnames';
import letter from '../../../assets/images/letter2.webp';
import file from '../../../assets/images/file2.webp';
import box from '../../../assets/images/box2.webp';
import Icon from '../../../components/Icon';
import { useNavigate } from 'react-router-dom';
import { handleFreqFeedbackReq } from './Alarm';

export const notiType = Object.freeze({
  REQUEST: 'frequentFeedbackRequest',
  REPORT: 'feedbackReportCreate',
  UNREAD: 'unreadFeedbackExist',
  NEW: 'feedbackReceive',
});

/**
 * 노티파이 컴포넌트
 * @param {Object} props
 * @param {object} props.banner
 * @param {Function} props.onClose
 */
export default function Banner({ banner, onClose }) {
  const navigate = useNavigate();

  const { notification, ids } = banner;

  // TODO: 파동 애니메이션 추가
  // TODO: delete 아이콘, 화살표 아이콘 수정
  const getContent = () => {
    switch (notification.type) {
      case notiType.UNREAD:
        return {
          image: letter,
          message: '확인하지 않은\n피드백이 있어요!',
          buttonText: '피드백 확인하기',
          routeAction: () => navigate('/feedback/received'),
        };
      case notiType.NEW:
        return {
          image: letter,
          message: '새로운 피드백이\n도착했어요!',
          buttonText: '피드백 확인하기',
          routeAction: () => navigate('/feedback/received'),
        };
      case notiType.REPORT:
        return {
          image: file,
          message: `${notification.receiverName}님의 피드백을\n정리했어요!`,
          buttonText: '피드백 리포트 확인하기',
          routeAction: () => navigate('/mypage/report'),
        };
      case notiType.REQUEST:
        return {
          image: box,
          message:
            ids.length > 1 ?
              `${notification.senderName}님 외 ${ids.length - 1}명이\n피드백을 요청했어요!`
            : `${notification.senderName}님이\n피드백을 요청했어요!`,
          buttonText: '피드백 보내기',
          routeAction: () =>
            handleFreqFeedbackReq(navigate, {
              teamId: notification.teamId,
              senderId: ids.length > 1 ? null : notification.senderId,
            }),
        };
      default:
        throw new Error('Invalid notiType');
    }
  };

  const { image, message, buttonText, routeAction } = getContent();

  return (
    <div
      className={classNames(
        'rounded-400 relative h-[148px] w-full pt-5 pb-4 pl-6',
        (
          notification.type === notiType.REQUEST ||
            notification.type === notiType.UNREAD
        ) ?
          'bg-gray-100'
        : 'bg-lime-500',
      )}
    >
      <div className='flex h-full w-4/7 flex-col justify-between'>
        <p className='header-4 line-clamp-2 whitespace-pre-line'>{message}</p>
        <button
          className='flex items-center gap-0.5 text-gray-600'
          onClick={() => {
            routeAction();
            onClose({ notificationIds: ids });
          }}
        >
          <p className='caption-2'>{buttonText}</p>
          <Icon name='chevronDown' className='-rotate-90' />
        </button>
      </div>
      <div className='absolute -top-8 right-6 flex flex-col items-center'>
        <img src={image} className='scale-50' />
        <div className='-mt-11 h-[30px] w-22 rounded-tl-[7px] rounded-tr-[7px] bg-gradient-to-b from-[#2a2a2a] from-60% to-transparent'>
          <p className='mt-1 text-center text-[10px] font-thin text-gray-100'>
            NEW
          </p>
        </div>
      </div>
      <button
        className='absolute top-4 right-4'
        onClick={() => onClose({ notificationIds: ids })}
      >
        <Icon name='delete' className='text-gray-500' />
      </button>
    </div>
  );
}
