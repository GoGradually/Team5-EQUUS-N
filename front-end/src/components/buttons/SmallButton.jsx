import classNames from 'classnames';
import Icon from '../Icon';

/**
 * 작은 버튼 컴포넌트
 * @param {object} props
 * @param {string} props.text - 버튼 텍스트
 * @param {boolean} props.disabled - 버튼 비활성화 여부
 * @param {function} props.onClick - 버튼 클릭 함수
 * @param {boolean} props.isOutlined - 테두리/배경 여부
 * @returns {JSX.Element} - 버튼 컴포넌트
 */
export default function SmallButton({
  text,
  disabled,
  onClick,
  isOutlined = true,
}) {
  return (
    <button
      disabled={disabled}
      className={classNames(
        'rounded-300 flex h-[52px] w-[115px] items-center justify-center text-gray-300',
        isOutlined ?
          disabled ? 'border border-gray-500 text-gray-500'
          : 'border border-lime-500 text-lime-500'
        : disabled ? 'bg-gray-800 text-gray-300'
        : 'bg-lime-500 text-gray-900',
      )}
      onClick={onClick}
    >
      {text}
    </button>
  );
}
