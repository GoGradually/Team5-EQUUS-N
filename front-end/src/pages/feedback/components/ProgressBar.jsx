/**
 * 프로그래스 바 컴포넌트
 * @param {object} props
 * @param {boolean} props.isVisible - 보이기 여부
 * @param {number} props.currentStep - 진행단계
 * @param {number} props.totalStep - 총 단계
 * @returns {ReactElement}
 */
export default function ProgressBar({
  isVisible = true,
  currentStep,
  totalStep,
}) {
  // TODO: 로직 붙일때 애니메이션 적용
  return (
    <div
      className={`mt-2 flex h-1 w-full items-center justify-center gap-5 ${!isVisible && 'invisible'}`}
    >
      {[...Array(totalStep)].map((_, i) => (
        <div
          key={i}
          className={`h-1 w-full rounded-full ${i + 1 === currentStep ? 'bg-lime-500' : 'bg-gray-600'}`}
        />
      ))}
    </div>
  );
}
