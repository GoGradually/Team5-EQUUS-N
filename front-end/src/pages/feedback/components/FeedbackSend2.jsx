import { useEffect, useReducer, useState } from 'react';
import { useFeedbackObjective } from '../../../api/useFeedback';
import KeywordButton from '../../../components/buttons/KeywordButton';
import { showToast } from '../../../utility/handleToast';
import FooterWrapper from '../../../components/wrappers/FooterWrapper';
import LargeButton from '../../../components/buttons/LargeButton';
import { useLocation, useNavigate } from 'react-router-dom';
import { AnimatePresence, motion } from 'motion/react';
import RequestedContent from './RequestedContent';

export default function FeedbackSend2() {
  const navigate = useNavigate();
  const locationState = useLocation().state;

  const { data: objectives } = useFeedbackObjective();

  const [selectedObjectives, setSelectedObjectives] = useState([]);
  const [isNextStep, jumpToNextStep] = useReducer(() => true, false);

  useEffect(() => {
    if (isNextStep) {
      setTimeout(() => {
        navigate('../3', {
          state: {
            ...locationState,
            objectiveFeedbacks: selectedObjectives,
          },
        });
      }, 500);
    }
  }, [isNextStep]);

  const onKeywordButtonClick = (keyword) => {
    selectedObjectives.includes(keyword) ?
      setSelectedObjectives([
        ...selectedObjectives.filter((item) => item !== keyword),
      ])
    : selectedObjectives.length < 5 ?
      setSelectedObjectives([...selectedObjectives, keyword])
    : showToast('키워드는 5개까지 선택 가능해요');
  };

  return (
    <div className='flex w-full flex-col gap-8 pb-28'>
      <h1 className='header-2 text-gray-0 mt-3 whitespace-pre-line'>
        {'보낼 피드백 키워드를\n선택해 주세요'}
      </h1>
      <AnimatePresence>
        {!isNextStep && objectives && (
          <ul className='scrollbar-hidden flex w-full gap-8 overflow-x-auto p-1 whitespace-nowrap'>
            {Object.keys(objectives[locationState.feedbackFeeling]).map(
              (title, index) => (
                <ObjectiveColumn
                  key={index}
                  index={index}
                  title={title}
                  keywords={objectives[locationState.feedbackFeeling][title]}
                  selectedKeywords={selectedObjectives}
                  onClick={onKeywordButtonClick}
                />
              ),
            )}
          </ul>
        )}
      </AnimatePresence>
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
          disabled={selectedObjectives.length === 0}
          onClick={() =>
            selectedObjectives.length === 0 ?
              showToast('키워드를 최소 한 개 선택해 주세요')
            : jumpToNextStep()
          }
        />
      </FooterWrapper>
    </div>
  );
}

const ObjectiveColumn = ({
  index,
  title,
  keywords,
  selectedKeywords,
  onClick,
}) => {
  return (
    <motion.li
      className='inline-block w-80'
      initial={{ opacity: 0, y: 30 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, x: -30 }}
      transition={{ type: 'spring', bounce: 0.32, delay: index * 0.1 }}
    >
      <h2 className='subtitle-1 text-gray-0 mb-3'>{title}</h2>
      <ul className='flex flex-col gap-2'>
        {keywords.map((keyword, index) => (
          <KeywordButton
            key={index}
            isActive={selectedKeywords.includes(keyword)}
            onClick={() => onClick(keyword)}
          >
            {keyword}
          </KeywordButton>
        ))}
      </ul>
    </motion.li>
  );
};
