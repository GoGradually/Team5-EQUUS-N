/**
 * AiButton 컴포넌트
 * @param {object} props
 * @param {boolean} props.isActive 액티브 여부
 * @param {function} props.onClick 클릭 이벤트
 * @param {boolean} props.disalbed 비활성화 여부
 * @param {string} props.children 버튼 내용
 * @returns {React.ReactElement}
 * @example <AiButton isActive={true} onClick={clickButton}>확인</AiButton>
 */
export default function AiButton({ isActive, onClick, disalbed, children }) {
  return (
    <button
      className={`rounded-300 button-2 size-fit px-2 py-1.5 ${
        isActive ?
          'border border-lime-500 text-lime-400'
        : 'bg-gray-800 text-gray-300'
      }`}
      onClick={onClick}
      disabled={disalbed}
    >
      {children}
    </button>
  );
}
