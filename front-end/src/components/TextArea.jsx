import { useState } from 'react';
import Icon from './Icon';
import { transformToBytes } from '../utility/inputChecker';

export default function TextArea({
  isWithGpt = false,
  isGptLoading = false,
  canToggleAnonymous = false,
  generatedByGpt = false,
  isAnonymous = false,
  textContent,
  toggleAnonymous,
  textLength,
  setTextLength,
  setTextContent,
}) {
  const [overflownIndex, setOverflownIndex] = useState();

  const onInput = (e) => {
    const { byteCount, overflowedIndex } = transformToBytes(e.target.value);
    setTextLength(byteCount);

    if (byteCount >= 399) {
      if (!overflownIndex) {
        setOverflownIndex(overflowedIndex);
      }
      if (overflownIndex)
        e.target.value = e.target.value.slice(0, overflownIndex);
    } else {
      if (overflownIndex) setOverflownIndex(null);
    }

    setTextContent(e.target.value);
  };

  const spinner = (
    <div className='absolute inset-0 flex flex-col items-center justify-center'>
      <div className='relative mt-6 mb-10 flex items-center justify-center'>
        <div className='absolute size-10 animate-spin rounded-full bg-conic from-transparent from-5% to-lime-600' />
        <div className='absolute size-[30px] rounded-full bg-gray-800' />
      </div>
      <p className='body-1 animate-pulse text-gray-300'>
        AI가 글을 다듬는 중...
      </p>
    </div>
  );

  return (
    <div
      className={`rounded-300 relative flex h-fit w-full flex-col border-white p-5 ring-gray-500 has-focus:ring-gray-300 ${generatedByGpt ? 'bg-gray-800' : 'ring'}`}
    >
      <textarea
        value={textContent}
        onInput={onInput}
        className={`text-gray-0 placeholder:body-1 scrollbar-hidden relative min-h-32 w-full resize-none outline-none placeholder:text-gray-500 focus:placeholder:text-gray-400`}
        placeholder={
          generatedByGpt ? undefined
          : isWithGpt ?
            '자유롭게 적고 AI를 통해 다듬어 보세요.(선택사항)'
          : '내용을 입력해주세요'
        }
        disabled={generatedByGpt}
      />
      <div className='mt-2 flex w-full justify-between'>
        {canToggleAnonymous ?
          <button className='flex items-center' onClick={toggleAnonymous}>
            <p className='body-1 mr-1.5 text-white'>익명</p>
            {isAnonymous ?
              <Icon name='checkBoxClick' />
            : <Icon name='checkBoxNone' />}
          </button>
        : <div />}
        <p
          className={`caption-1 text-gray-300 ${isGptLoading && 'invisible'}`}
        >{`${textLength}/400 byte`}</p>
      </div>
      {isGptLoading && spinner}
    </div>
  );
}
