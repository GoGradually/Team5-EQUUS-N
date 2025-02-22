import classNames from 'classnames';
import letter from '../../../assets/images/letter2.webp';
import file from '../../../assets/images/file2.webp';
import box from '../../../assets/images/box2.webp';
import Icon from '../../../components/Icon';
import { useNavigate } from 'react-router-dom';
import { handleFreqFeedbackReq } from './Alarm';
import { motion } from 'motion/react';

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

  // TODO: 파동 애니메이션..?
  const getContent = () => {
    switch (notification.type) {
      case notiType.UNREAD:
        return {
          image: letter,
          message: '확인하지 않은\n피드백이 있어요!',
          buttonText: '피드백 확인하기',
          routeAction: () => navigate('/feedback/received'),
          needColorfulWave: true,
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
          needColorfulWave: true,
        };
      default:
        throw new Error('Invalid notiType');
    }
  };

  const { image, message, buttonText, routeAction, needColorfulWave } =
    getContent();

  const eyesAnime = (
    <div className='flex h-[17px] w-3 flex-col items-end justify-center rounded-[50%] bg-white shadow-sm shadow-black/4'>
      <motion.div
        animate={{ x: [0, -6, -6, 0], height: [8, 1, 8] }}
        transition={{
          x: {
            duration: 0.7,
            repeat: Infinity,
            repeatDelay: 2,
            ease: 'easeInOut',
          },
          height: {
            duration: 0.3,
            repeat: Infinity,
            repeatDelay: 5.1,
            delay: 0.8,
            ease: 'easeInOut',
          },
        }}
        className='h-2 w-[6.4px] rounded-[50%] bg-gray-600'
      />
    </div>
  );

  const waveAnime = (
    <motion.div
      className={`absolute top-14 size-20 rounded-full border-2 ${needColorfulWave ? 'border-lime-500' : 'border-white'}`}
      animate={{ scale: [0.5, 1.2], opacity: [0, 1, 0] }}
      transition={{
        duration: 1.2,
        repeat: Infinity,
        ease: 'easeOut',
        repeatDelay: 0.8,
        delay: 0.4,
      }}
    />
  );

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
      <div className='absolute top-4.5 right-30 flex -rotate-12'>
        {eyesAnime}
        {eyesAnime}
      </div>
      <div className='absolute -top-7 right-6 flex flex-col items-center'>
        <motion.img
          src={image}
          className='z-10 scale-50'
          animate={{ y: [0, 20, 0] }}
          transition={{ duration: 2, repeat: Infinity, ease: 'easeInOut' }}
        />
        <div className='z-10 -mt-11 h-[30px] w-22 rounded-tl-[7px] rounded-tr-[7px] bg-gradient-to-b from-[#2a2a2a] from-60% to-transparent'>
          <p className='mt-1 text-center text-[10px] font-thin text-gray-100'>
            NEW
          </p>
        </div>
        {waveAnime}
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
