import { useEffect, useReducer, useState } from 'react';
import FeedBackButton from '../../../components/buttons/FeedBackButton';
import FooterWrapper from '../../../components/wrappers/FooterWrapper';
import LargeButton from '../../../components/buttons/LargeButton';
import { useLocation, useNavigate } from 'react-router-dom';
import { showToast } from '../../../utility/handleToast';
import { motion } from 'motion/react';
import RequestedContent from './RequestedContent';

export default function FeedbackSend1() {
  const navigate = useNavigate();
  const locationState = useLocation().state;

  const [feedbackFeeling, setFeedbackFeeling] = useState();
  const [isNextStep, jumpToNextStep] = useReducer(() => true, false);

  useEffect(() => {
    if (isNextStep) {
      setTimeout(() => {
        navigate('../2', {
          state: { ...locationState, feedbackFeeling },
        });
      }, 400);
    }
  }, [isNextStep]);

  return (
    <div className='flex w-full flex-col gap-8 pb-28'>
      <h1 className='header-2 text-gray-0 mt-3 whitespace-pre-line'>
        {`${locationState.receiver.name}님에게\n어떤 피드백을 보낼까요?`}
      </h1>
      <FeedBackButton
        currentFeedback={feedbackFeeling}
        isNextStep={isNextStep}
        onClick={(feeling) => setFeedbackFeeling(feeling)}
      />
      {locationState.requestedContent && !isNextStep && (
        <motion.details
          className='flex flex-col'
          initial={{ opacity: 0 }}
          animate={{ opacity: 1, transition: { delay: 0.5 } }}
        >
          <RequestedContent content={locationState.requestedContent} />
        </motion.details>
      )}

      <FooterWrapper>
        <LargeButton
          isOutlined={false}
          text='다음'
          disabled={!feedbackFeeling}
          onClick={() =>
            feedbackFeeling ? jumpToNextStep() : (
              showToast('피드백 종류를 선택해 주세요')
            )
          }
        />
      </FooterWrapper>
    </div>
  );
}
