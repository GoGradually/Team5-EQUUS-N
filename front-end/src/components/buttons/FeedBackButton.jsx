import { AnimatePresence, motion } from 'motion/react';

/**
 * 피드백 버튼 컴포넌트
 * @param {object} props
 * @param {string} props.currentFeedback - 현재 선택된 피드백
 * @param {boolean} props.isNextStep - 다음 단계 여부
 * @param {function} props.onClick - 피드백 선택 함수
 * @returns {JSX.Element} - 피드백 버튼 컴포넌트
 */
export default function FeedBackButton({
  currentFeedback,
  isNextStep,
  onClick,
}) {
  return (
    <div className='flex w-full gap-3'>
      <AnimatePresence>
        {!isNextStep && (
          <>
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, x: -30 }}
              transition={{ type: 'spring', bounce: 0.32 }}
              className={`rounded-400 flex flex-1 cursor-pointer flex-col items-center justify-center gap-7 bg-gray-800 px-6.5 py-9 ring-lime-500 transition-shadow duration-200 ${
                currentFeedback === '칭찬해요' && 'ring-2'
              }`}
              onClick={() => {
                onClick('칭찬해요');
              }}
            >
              <p className='text-4xl'>😀</p>
              <div className='flex flex-col items-center gap-3'>
                <p className='caption-1 text-center break-keep whitespace-pre-line text-gray-300'>
                  {'개인의 강점과\n행동을 칭찬해주는'}
                </p>
                <p className='header-4 text-lime-500'>칭찬해요</p>
              </div>
            </motion.div>
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, x: -30 }}
              transition={{ type: 'spring', bounce: 0.32, delay: 0.1 }}
              className={`rounded-400 flex flex-1 cursor-pointer flex-col items-center justify-center gap-7 bg-gray-800 px-6.5 py-9 ring-lime-500 transition-shadow duration-200 ${
                currentFeedback === '아쉬워요' && 'ring-2'
              }`}
              onClick={() => {
                onClick('아쉬워요');
              }}
            >
              <p className='text-4xl'>🤔</p>
              <div className='flex flex-col items-center gap-3'>
                <p className='caption-1 text-center break-keep whitespace-pre-line text-gray-300'>
                  {'존중하는 말투로\n개선 방법을 제공하는'}
                </p>
                <p className='header-4 text-lime-500'>아쉬워요</p>
              </div>
            </motion.div>
          </>
        )}
      </AnimatePresence>
    </div>
  );
}
