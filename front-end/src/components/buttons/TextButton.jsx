import Icon from '../Icon';

export const TextButtonType = Object.freeze({
  CHECK: 'CHECK',
  PLUS: 'PLUS',
  DEFAULT: 'DEFAULT',
});

/**
 * 텍스트 버튼 컴포넌트
 * @param {object} props
 * @param {keyof typeof TextButtonType} props.type - 버튼 타입
 * @param {() => void} props.onClick - 클릭 이벤트
 * @param {React.ReactNode} props.children - 버튼 내용
 * @returns {React.ReactElement}
 * @example <TextButton type={TextButtonType.PLUS} onClick={() => console.log('click')}>
 */
export default function TextButton({ type, onClick, children }) {
  return (
    <button
      className='subtitle-2 flex w-full justify-between py-3 text-gray-300 transition hover:text-lime-500'
      onClick={onClick}
    >
      {children}
      {type === TextButtonType.DEFAULT && <div />}
      {type === TextButtonType.PLUS && (
        <Icon name={'plusM'} color={'lime-700'} />
      )}
      {type === TextButtonType.CHECK && (
        <Icon name={'check'} color={'gray-200'} />
      )}
    </button>
  );
}
