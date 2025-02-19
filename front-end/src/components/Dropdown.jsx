import { useRef, useState, forwardRef, useEffect } from 'react';
import Icon from './Icon';

export function DropdownSmall({ triggerText, setTriggerText, items }) {
  const detailsRef = useRef(null);

  return (
    <details ref={detailsRef} className='group'>
      <summary className='rounded-200 caption-2 flex size-fit cursor-pointer list-none items-center bg-gray-700 py-1 pr-1 pl-2.5 text-gray-200'>
        {triggerText}
        <Icon
          name='chevronDown'
          className='ml-0.5 transition group-open:rotate-180'
        />
      </summary>
      <ul className='rounded-200 absolute z-20 mt-2 flex size-fit flex-col gap-1 bg-gray-700 p-2'>
        {[...items, '전체 보기'].map((item, index) => (
          <li
            key={index}
            onClick={() => {
              detailsRef.current.open = false;
              setTriggerText(item);
            }}
            className={`body-1 cursor-pointer list-none px-2.5 py-1 ${item === triggerText ? 'text-gray-200' : 'text-gray-400'}`}
          >
            {item}
          </li>
        ))}
      </ul>
      {/* 배경 클릭을 위한 빽드롭필터 */}
      <div
        className='fixed inset-0 z-10'
        onClick={() => (detailsRef.current.open = false)}
      />
    </details>
  );
}

export function DropdownLarge({
  triggerText,
  setTriggerText,
  isFromTime = true,
  isTransparent = false,
  itemsComponent,
  items,
}) {
  const detailsRef = useRef(null);
  const selectedItemRef = useRef(null);

  // details 태그가 열릴 때마다 선택한 항목으로 스크롤 이동
  const handleToggle = () => {
    if (selectedItemRef.current) {
      const selectedItem = selectedItemRef.current;

      selectedItem.scrollIntoView({
        behavior: 'smooth',
        block: 'center',
      });
    }
  };

  return (
    <details ref={detailsRef} onToggle={handleToggle} className='group flex-1'>
      <summary
        className={`${isTransparent ? 'border border-gray-300' : 'bg-gray-700'} rounded-200 body-1 flex size-fit w-full cursor-pointer list-none items-center justify-between px-5 py-3.5 whitespace-pre text-gray-200`}
      >
        <p>
          {triggerText} <span>{isFromTime ? ' 부터' : ' 까지'}</span>
        </p>
        <Icon
          name='chevronDown'
          className='ml-0.5 transition group-open:rotate-180'
        />
      </summary>
      <div className='absolute z-20 mt-2'>
        {itemsComponent ?
          itemsComponent
        : <ul className='rounded-200 flex max-h-56 w-[120px] flex-col gap-1 overflow-y-auto bg-gray-700 p-2'>
            {items.map((item, index) => (
              <li
                key={index}
                ref={item === triggerText ? selectedItemRef : null}
                onClick={() => {
                  detailsRef.current.open = false;
                  setTriggerText(item);
                }}
                className={`body-1 cursor-pointer list-none px-2.5 py-2 text-center ${item === triggerText ? 'text-lime-500' : 'text-gray-0'}`}
              >
                {item}
              </li>
            ))}
          </ul>
        }
      </div>
      {/* 배경 클릭을 위한 빽드롭필터 */}
      <div
        className='fixed inset-0 z-10'
        onClick={() => (detailsRef.current.open = false)}
      />
    </details>
  );
}
