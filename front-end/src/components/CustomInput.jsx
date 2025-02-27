import Icon from './Icon';
import classNames from 'classnames';
import { forwardRef } from 'react';

/**
 * @param {object} props
 * @param {string} props.label - 제목
 * @param {string} props.hint - 플레이스홀더
 * @param {string} props.content - 초기값
 * @param {function} props.setContent - 값 변경 함수
 * @param {string} props.keyboardType - 키보드 타입
 * @param {ReactNode} props.addOn - 오른쪽 아이콘
 * @param {function[]} props.condition - 하단 조건
 * @param {string[]} props.notification - 하단 조건 알림
 * @param {boolean} props.isPassword - 비밀번호 여부
 * @param {boolean} props.disabled - 비활성화 여부
 * @param {boolean} props.isOutlined - 테두리/배경 여부
 * @param {boolean} props.isForRetrospect - 회고 작성용
 * @param {string} props.bgColor - 배경색
 * @param {function} props.onEnter - 엔터 키 눌렀을 때 호출되는 함수
 * @param {React.Ref} ref - ref 매개변수
 * @returns
 */
const CustomInput = forwardRef(function CustomInput(
  {
    label,
    hint,
    content = '',
    setContent,
    keyboardType = 'text',
    addOn,
    condition,
    notification,
    isPassword,
    disabled = setContent ? false : true, // setContent가 없으면 비활성화
    isOutlined = true,
    isForRetrospect = false,
    bgColor = 'gray-800',
    onEnter = () => {},
  },
  ref,
) {
  const handleEnter = (event) => {
    if (event.key === 'Enter') {
      onEnter();
    }
  };

  return (
    <div className='flex flex-col gap-2'>
      {/* 제목 */}
      {label && <p className='subtitle-2 text-gray-0'>{label}</p>}
      {/* 인풋 */}
      <div className='group relative flex flex-col gap-2'>
        <input
          ref={ref}
          className={classNames(
            'rounded-300 text-gray-0 flex h-[52px] w-full items-center border caret-gray-300',
            // 오른쪽 아이콘 있으면 입력 길이를 줄임
            addOn ? 'pr-14 pl-5' : 'px-5',
            // 테두리 여부
            isOutlined ?
              `border-gray-${isForRetrospect ? '500' : '600'}`
            : `border-none bg-${bgColor}`,
            // 포커스 시 스타일
            'placeholder:text-gray-500 focus:border-gray-300 focus:outline-none focus:placeholder:text-gray-400',
          )}
          placeholder={hint}
          value={content}
          type={isPassword ? 'password' : 'text'}
          inputMode={keyboardType}
          onChange={(e) => setContent(e.target.value)}
          disabled={disabled}
          onKeyDown={handleEnter}
        ></input>
        {/* 하단 조건 */}
        {condition && (
          <div className='flex justify-end text-right'>
            {condition.map((item, index) => (
              <div key={index} className='flex'>
                {index > 0 && (
                  <p
                    className={`caption-1 ${content.length === 0 ? 'text-gray-500' : 'text-gray-100'}`}
                  >
                    &nbsp;•&nbsp;
                  </p>
                )}
                <p
                  className={`caption-1 ${
                    content.length === 0 ? 'text-gray-500'
                    : item(content) ? 'text-lime-300'
                    : 'text-red-300'
                  }`}
                >
                  {notification[index]}
                </p>
              </div>
            ))}
          </div>
        )}
        {/* 오른쪽 아이콘 */}
        {addOn && <div className='absolute top-[14px] right-5'>{addOn}</div>}
      </div>
    </div>
  );
});

// 컴포넌트 이름 설정 (개발 도구에서 확인용)
CustomInput.displayName = 'CustomInput';

export default CustomInput;
