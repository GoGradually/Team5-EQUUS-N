import { useEffect, useState } from 'react';

/**
 * 토스트 컴포넌트
 * @param {object} props
 * @param {string} props.content - 토스트 내용
 * @returns {JSX.Element} - 토스트 컴포넌트
 */
export default function Toast({ content }) {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    setIsVisible(true);
    const timer = setTimeout(() => {
      setIsVisible(false);
    }, 2000);

    return () => clearTimeout(timer);
  }, []);

  return (
    <div
      className={`body-3 text-gray-0 fixed right-0 bottom-[110px] left-0 z-2000 mx-auto flex w-fit items-center justify-center rounded-full bg-gray-700 px-5 transition-all duration-300 ease-in-out`}
      style={{
        height: '38px',
        transform: `translateY(${isVisible ? '0' : '200px'})`,
      }}
    >
      {content}
    </div>
  );
}
