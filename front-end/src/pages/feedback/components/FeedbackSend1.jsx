import { useState } from 'react';
import FeedBackButton from '../../../components/buttons/FeedBackButton';
import FooterWrapper from '../../../components/wrappers/FooterWrapper';
import LargeButton from '../../../components/buttons/LargeButton';
import { useLocation, useNavigate } from 'react-router-dom';

export default function FeedbackSend1() {
  const navigate = useNavigate();
  const locationState = useLocation().state;

  const [feedbackFeeling, setFeedbackFeeling] = useState();

  return (
    <div className='flex size-full flex-col gap-8'>
      <h1 className='header-2 text-gray-0 mt-3 whitespace-pre-line'>
        {`${locationState.receiver.name}님에게\n어떤 피드백을 보낼까요?`}
      </h1>
      <FeedBackButton
        currentFeedback={feedbackFeeling}
        onClick={(feeling) => setFeedbackFeeling(feeling)}
      />
      <FooterWrapper>
        <LargeButton
          isOutlined={false}
          text='다음'
          disabled={!feedbackFeeling}
          onClick={() =>
            feedbackFeeling &&
            navigate('../2', {
              state: { ...locationState, feedbackFeeling },
            })
          }
        />
      </FooterWrapper>
    </div>
  );
}
