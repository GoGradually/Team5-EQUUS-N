import { useState } from 'react';
import Icon from './Icon';
import { transformToBytes } from '../utility/inputChecker';
import Spinner from './Spinner';

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
  gptContents,
  setGptContents,
}) {
  const onInput = (e) => {
    const value = e.target.value;
    const { byteCount, overflowedIndex } = transformToBytes(value, 400);
    if (overflowedIndex !== -1) {
      setTextLength(byteCount);
      setTextContent(value.slice(0, overflowedIndex));
    } else {
      setTextLength(byteCount);
      setTextContent(value);
    }
  };

  const spinner = (
    <Spinner>
      <p className='body-1 animate-pulse text-gray-300'>
        AI가 글을 다듬는 중...
      </p>
    </Spinner>
  );

  const normalTextArea = (
    <>
      <textarea
        value={textContent}
        onInput={onInput}
        className={`text-gray-0 placeholder:body-1 scrollbar-hidden relative min-h-32 w-full resize-none outline-none placeholder:text-gray-500 focus:placeholder:text-gray-400`}
        placeholder={
          isWithGpt ?
            '자유롭게 적고 AI를 통해 다듬어 보세요.(선택사항)'
          : '내용을 입력해주세요'
        }
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
    </>
  );

  const gptTextArea = (
    <>
      <textarea
        value={isGptLoading ? '' : gptContents?.contents[gptContents.index]}
        className={`text-gray-0 placeholder:body-1 scrollbar-hidden relative min-h-32 w-full resize-none outline-none placeholder:text-gray-500 focus:placeholder:text-gray-400`}
        disabled={true}
      />
      <div
        className={`mt-2 flex w-full justify-between ${isGptLoading && 'invisible'}`}
      >
        <div
          className={`flex gap-1 text-white ${gptContents?.contents.length <= 1 && 'invisible'}`}
        >
          <button
            onClick={() =>
              setGptContents((prev) => {
                return {
                  ...prev,
                  index: Math.max(
                    0,
                    Math.min(prev.index - 1, prev.contents.length - 1),
                  ),
                };
              })
            }
          >
            <Icon name='chevronDown' className='rotate-90' />
          </button>
          <p>{`${gptContents?.index + 1}/${gptContents?.contents.length}`}</p>
          <button
            onClick={() =>
              setGptContents((prev) => {
                return {
                  ...prev,
                  index: Math.max(
                    0,
                    Math.min(prev.index + 1, prev.contents.length - 1),
                  ),
                };
              })
            }
          >
            <Icon name='chevronDown' className='-rotate-90' />
          </button>
        </div>
        <p className='caption-1 text-gray-300'>{`${
          transformToBytes(gptContents?.contents[gptContents.index] ?? '')
            .byteCount
        }/400 byte`}</p>
      </div>
      {isGptLoading && spinner}
    </>
  );

  return (
    <div
      className={`rounded-300 relative flex h-fit w-full flex-col border-white p-5 ring-gray-500 has-focus:ring-gray-300 ${generatedByGpt ? 'bg-gray-800' : 'ring'}`}
    >
      {generatedByGpt ? gptTextArea : normalTextArea}
    </div>
  );
}
