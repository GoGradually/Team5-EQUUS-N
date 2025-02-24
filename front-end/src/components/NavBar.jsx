import Icon from './Icon';

/**
 * 네비게이션 바 2 컴포넌트
 * @param {object} props
 * @param {boolean} props.canPop - 뒤로 가기 가능 여부
 * @param {boolean} props.canClose - 닫기 가능 여부
 * @param {boolean} props.isCloseLeft - 닫기 왼쪽 여부
 * @param {string} props.title - 네비게이션 바 제목
 * @param {function} props.onClickPop - 뒤로 가기 클릭 함수
 * @param {function} props.onClickClose - 닫기 클릭 함수
 * @returns {JSX.Element} - 네비게이션 바 2 컴포넌트
 */
export default function NavBar({
  canPop,
  canClose,
  isCloseLeft,
  title = '',
  onClickPop,
  onClickClose,
}) {
  return (
    <div className='h-navBar flex w-full items-center justify-center'>
      <button
        className='pr-8'
        onClick={
          canPop ? onClickPop
          : canClose ?
            onClickClose
          : null
        }
      >
        {canPop && !isCloseLeft ?
          <Icon name={'chevronLeft'} color={'var(--color-gray-100)'} />
        : null}
        {canClose && isCloseLeft ?
          <Icon name={'delete'} color={'var(--color-gray-100)'} />
        : null}
      </button>
      <div className='flex flex-1 items-center justify-center'>
        <p className='subtitle-2 text-gray-100'>{title}</p>
      </div>
      {canClose && !isCloseLeft ?
        <button className='m-4 h-6 w-6' onClick={onClickClose}>
          <Icon name={'delete'} color={'var(--color-gray-100)'} />
        </button>
      : <div className='m-4 h-6 w-6' />}
    </div>
  );
}
