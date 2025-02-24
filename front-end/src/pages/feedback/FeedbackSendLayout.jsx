import NavBar from '../../components/NavBar';
import StickyWrapper from '../../components/wrappers/StickyWrapper';
import LargeButton from '../../components/buttons/LargeButton';
import FooterWrapper from '../../components/wrappers/FooterWrapper';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import ProgressBar from './components/ProgressBar';
import { useEffect, useState } from 'react';

export default function FeedbackSendLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const lastPath = location.pathname.split('/').at(-1);

  return (
    <div className='flex size-full flex-col'>
      <StickyWrapper>
        <ProgressBar
          currentStep={parseInt(lastPath) ?? 1}
          totalStep={3}
          isVisible={lastPath !== 'send' && lastPath !== 'frequent'}
        />
        <NavBar
          canPop={true}
          onClickPop={() => navigate(-1)}
          title={
            lastPath === 'send' ? '피드백 작성하기' : (
              lastPath === 'frequent' && '요청한 피드백'
            )
          }
        />
      </StickyWrapper>
      <Outlet />
    </div>
  );
}
