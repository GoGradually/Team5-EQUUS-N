import { useEffect, useRef, useState } from 'react';
import TextButton, { TextButtonType } from './buttons/TextButton';
import Icon from './Icon';

/**
 * 아코디언 컴포넌트... 그냥 네비바 같은디..?
 * @param {object} props
 * @param {boolean} props.isMainPage - 메인 페이지 여부
 * @param {number} props.selectedTeamId - 선택된 팀 ID
 * @param {array} props.teamList - 팀 목록
 * @param {function} props.onTeamClick - 팀 클릭 시 호출되는 함수
 * @param {boolean} props.isAlarmRead - 알람 읽음 여부
 * @param {boolean} props.canClose - 닫기 버튼 여부
 * @param {function} props.onClickLastButton - 마지막 버튼 클릭 시 호출되는 함수
 * @returns
 */
export default function Accordion({
  isMainPage,
  selectedTeamId,
  teamList = [],
  onTeamClick,
  isAlarmRead = false,
  canClose = true,
  onClickLastButton,
}) {
  const detailsRef = useRef(null);

  useEffect(() => {
    if (isMainPage) {
      // 만약 메인페이지면 알람 api get 요청 후 setIsAlarmRead() 호출
    }
  }, [isMainPage]);

  return (
    <header className='relative flex h-[60px] w-full items-center justify-between'>
      {teamList.length === 0 ?
        <Icon name='logo' />
      : <details ref={detailsRef} className='group z-0'>
          <summary className='header-3 flex cursor-pointer list-none items-center gap-0.5 text-white'>
            {teamList.find((team) => team.id === selectedTeamId)?.name ??
              '선택 안됨'}
            <Icon
              name='unfoldMore'
              className='transition group-open:rotate-180'
            />
          </summary>
          <div className='rounded-400 scrollbar-hidden absolute top-full flex max-h-96 w-full flex-col divide-y-1 divide-gray-600 overflow-y-auto bg-gray-800 px-5'>
            {teamList.map((team) => (
              <TextButton
                key={team.id}
                type={
                  team.id === selectedTeamId ?
                    TextButtonType.CHECK
                  : TextButtonType.DEFAULT
                }
                onClick={() => {
                  onTeamClick(team.id);
                  detailsRef.current.open = false;
                }}
              >
                {team.name}
              </TextButton>
            ))}
            <TextButton
              type={isMainPage ? TextButtonType.PLUS : TextButtonType.DEFAULT}
              onClick={() => onClickLastButton()}
            >
              {isMainPage ? '새로운 팀 스페이스 만들기' : '전체 일정 보기'}
            </TextButton>
          </div>
          {/* 빽드롭필터 */}
          <div
            className='fixed inset-0 -z-10 bg-black/60'
            onClick={() => (detailsRef.current.open = false)}
          />
        </details>
      }
      {isMainPage ?
        <div className='flex gap-4 divide-gray-600'>
          {teamList.length > 0 && (
            <button
              onClick={() => {
                console.log('알람 페이지로 이동');
              }}
            >
              <Icon name={isAlarmRead ? 'bellOn' : 'bellOff'} />
            </button>
          )}
          <button onClick={() => console.log('마이페이지로 이동')}>
            <Icon name='hamburger' />
          </button>
        </div>
      : canClose ?
        <button onClick={() => console.log('뒤로가기')}>
          <Icon name='delete' color='var(--color-gray-100)' />
        </button>
      : null}
    </header>
  );
}
