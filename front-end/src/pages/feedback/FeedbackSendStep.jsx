import { useLocation } from 'react-router-dom';
import FeedbackSend1 from './components/FeedbackSend1';
import FeedbackSend2 from './components/FeedbackSend2';
import FeedbackSend3 from './components/FeedbackSend3';

export default function FeedbackSendStep() {
  const location = useLocation();
  const currentStep = parseInt(location.pathname.split('/').at(-1));

  return (
    <div className='flex size-full flex-col'>
      {currentStep === 1 && <FeedbackSend1 />}
      {currentStep === 2 && <FeedbackSend2 />}
      {currentStep === 3 && <FeedbackSend3 />}
    </div>
  );
}
