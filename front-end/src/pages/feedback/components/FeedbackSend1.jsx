import { useEffect, useReducer, useState } from 'react';
import FeedBackButton from '../../../components/buttons/FeedBackButton';
import FooterWrapper from '../../../components/wrappers/FooterWrapper';
import LargeButton from '../../../components/buttons/LargeButton';
import { useLocation, useNavigate } from 'react-router-dom';
import { showToast } from '../../../utility/handleToast';
import Icon from '../../../components/Icon';
import Tag, { TagType } from '../../../components/Tag';
import AiButton from '../../../components/buttons/AiButton';
import TextArea from '../../../components/TextArea';

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
    <div className='flex size-full flex-col gap-8'>
      <h1 className='header-2 text-gray-0 mt-3 whitespace-pre-line'>
        {`${locationState.receiver.name}님에게\n어떤 피드백을 보낼까요?`}
      </h1>
      <FeedBackButton
        currentFeedback={feedbackFeeling}
        isNextStep={isNextStep}
        onClick={(feeling) => setFeedbackFeeling(feeling)}
      />
      <details className='flex flex-col gap-3'>
        <summary className='flex size-fit cursor-pointer list-none items-center gap-2'>
          <Icon name='info' />
          <p className='rounded-300 button-2 size-fit bg-gray-800 px-2 py-1.5 text-gray-300'>
            상대방이 요청한 피드백이에요
          </p>
        </summary>
        <TextArea />
      </details>

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
