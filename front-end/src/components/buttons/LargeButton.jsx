import classNames from 'classnames';
import Icon from '../Icon';

/**
 * 큰 버튼 컴포넌트
 * @param {object} props
 * @param {string} props.text - 버튼 텍스트
 * @param {boolean} props.disabled - 버튼 비활성화 여부
 * @param {boolean} props.realDisabled - 버튼 "진짜" 비활성화 여부
 * @param {function} props.onClick - 버튼 클릭 함수
 * @param {boolean} props.isOutlined - 테두리/배경 여부
 * @returns {JSX.Element} - 버튼 컴포넌트
 */
export default function LargeButton({
  text,
  disabled = false,
  realDisabled,
  onClick,
  isOutlined = true,
}) {
  return (
    <button
      disabled={realDisabled}
      className={classNames(
        'button-1 rounded-300 flex h-[56px] w-full items-center justify-center px-4 py-2 text-gray-300 transition duration-300',
        isOutlined ?
          disabled ? 'border border-gray-500 text-gray-500'
          : 'cursor-pointer border border-lime-500 text-lime-500'
        : disabled ? 'bg-gray-800 text-gray-300'
        : 'cursor-pointer bg-lime-500 text-gray-900',
        realDisabled && 'cursor-not-allowed',
      )}
      onClick={onClick}
    >
      {text ?
        text
      : <Icon
          name='plusS'
          color={
            isOutlined ?
              disabled ?
                'gray-500'
              : 'lime-500'
            : disabled ?
              'gray-100'
            : 'gray-900'
          }
        />
      }
    </button>
  );
}
