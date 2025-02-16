import { useLocation, useNavigate } from 'react-router-dom';
import LargeButton from '../../components/buttons/LargeButton';
import FooterWrapper from '../../components/wrappers/FooterWrapper';

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
      <div className='flex size-48 items-center justify-center rounded-full bg-lime-500 p-10'>
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
      </div>
      <h1 className='header-2 my-6 text-gray-100'>{completeType[typeKey]}</h1>
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
