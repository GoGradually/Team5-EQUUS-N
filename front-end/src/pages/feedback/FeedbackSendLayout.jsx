import NavBar2 from '../../components/NavBar2';
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

  console.log(location.state);

  return (
    <div className='flex size-full flex-col'>
      <StickyWrapper>
        <ProgressBar
          currentStep={parseInt(lastPath) ?? 1}
          totalStep={3}
          isVisible={lastPath !== 'send'}
        />
        <NavBar2
          canPop={true}
          onClickPop={() => navigate(-1)}
          title={lastPath === 'send' && '피드백 작성하기'}
        />
      </StickyWrapper>
      <Outlet />
    </div>
  );
}
