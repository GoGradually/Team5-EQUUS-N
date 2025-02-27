/**
 * 키워드 버튼 컴포넌트
 * @param {object} props
 * @param {boolean} props.isActive - 액티브 여부
 * @param {() => void} props.onClick - 클릭 이벤트
 * @param {React.ReactNode} props.children - 버튼 내용
 */
export default function KeywordButton({ isActive, onClick, children }) {
  return (
    <button
      className={`rounded-200 size-fit bg-gray-800 px-3 py-1.5 transition duration-200 ${
        isActive ?
          'body-1 text-lime-500 ring ring-lime-200/60'
        : 'body-1 text-gray-300'
      }`}
      onClick={onClick}
    >
      {children}
    </button>
  );
}
