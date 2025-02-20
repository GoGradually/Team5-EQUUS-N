import { useLocation, useNavigate } from 'react-router-dom';
import LargeButton from '../../components/buttons/LargeButton';
import FooterWrapper from '../../components/wrappers/FooterWrapper';
import { motion } from 'motion/react';

export const completeType = Object.freeze({
  REQUEST: '피드백 요청 완료!',
  SEND: '피드백 전송 완료!',
  RETROSPECT: '회고 작성 완료!',
});

export default function FeedbackComplete() {
  const location = useLocation();
  const navigate = useNavigate();

  const queryParams = new URLSearchParams(location.search);
  const typeKey = queryParams.get('type');

  return (
    <div className='flex size-full flex-col items-center justify-center'>
      <motion.div
        className='flex size-48 items-center justify-center rounded-full bg-lime-500 p-10'
        initial={{ opacity: 0, scale: 0.5, y: 330 }}
        animate={{ opacity: 1, scale: 1, y: 0 }}
        transition={{ duration: 0.5, ease: 'backOut' }}
      >
        <svg
          xmlns='http://www.w3.org/2000/svg'
          viewBox='0 0 24 24'
          fill='none'
          stroke='currentColor'
          strokeWidth='6'
          strokeLinecap='round'
          strokeLinejoin='round'
          className='size-full stroke-white'
        >
          <polyline points='20 6 11 17 4 12' />
        </svg>
      </motion.div>
      <motion.h1
        className='header-2 my-6 text-gray-100'
        initial={{ opacity: 0 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ type: 'spring', bounce: 0.32, delay: 0.5 }}
      >
        {completeType[typeKey]}
      </motion.h1>
      <FooterWrapper>
        <LargeButton
          isOutlined={false}
          text='확인'
          onClick={() => navigate('/main')}
        />
      </FooterWrapper>
    </div>
  );
}
