import { useEffect } from 'react';
import { useGetSchedules } from '../../../api/useCalendar';
import { useTeam } from '../../../useTeam';
import { changeDayName, getDateInfo, toKST } from '../../../utility/time';
import classNames from 'classnames';

/**
 * 요일 컴포넌트
 * @param {object} props
 * @param {number} props.dayIndex - 요일 인덱스 (0: 일요일, 1: 월요일, ...)
 * @returns {JSX.Element} - 요일 컴포넌트
 */
function CalenderDay({ dayIndex }) {
  let dayString = '월';
  dayString = changeDayName(dayIndex);
  return (
    <div className='caption-1 mx-auto w-9 text-center text-gray-200'>
      {dayString}
    </div>
  );
}

/**
 * 날짜 컴포넌트
 * @param {object} props
 * @param {Date} props.date - 날짜
 * @param {boolean} props.isSelected - 선택된 날짜인지 여부
 * @param {boolean} props.haveSchedule - 스케줄이 있는지 여부
 * @returns {JSX.Element} - 날짜 컴포넌트
 */
function CalendarDate({ date, isSelected, haveSchedule }) {
  const today = new Date(new Date().setHours(0, 0, 0, 0));
  const isToday =
    today.toISOString().split('T')[0] === date.toISOString().split('T')[0];
  return (
    <div className='flex flex-col gap-1'>
      <div
        className={classNames(
          'subtitle-2 rounded-200 relative flex aspect-square h-full w-full items-center justify-center text-gray-100',
          isSelected && 'border border-lime-500 text-lime-500',
          isToday &&
            'before:rounded-200 before:absolute before:inset-0 before:bg-lime-500 before:opacity-10',
        )}
      >
        {date.getDate()}
      </div>
      {haveSchedule ?
        <div className='h-1 w-full rounded-full bg-lime-500'></div>
      : <div className='h-1 w-full'> </div>}
    </div>
  );
}

/**
 * 주 컴포넌트
 * @param {object} props
 * @param {Date} props.curSunday - 현재 주의 일요일
 * @param {Date} props.selectedDate - 선택된 날짜
 * @param {function} props.setSelectedDate - 선택된 날짜 설정 함수
 * @param {Set} props.scheduleSet - 일정 집합
 * @param {function} props.setAllSchedules - 전체 일정 설정 함수
 * @param {boolean} props.setIsLoading - 로딩 상태
 * @returns {JSX.Element} - 주 컴포넌트
 */
export function CalendarWeek({
  curSunday,
  selectedDate,
  setSelectedDate,
  scheduleSet,
  setAllSchedules,
  setIsLoading,
}) {
  const dateList = getDateList(curSunday);

  const {
    data: schedules,
    isLoading: isSchedulesLoading,
    refetch,
  } = useGetSchedules({
    startDay: toKST(dateList[0]).toISOString().split('T')[0],
    endDay: toKST(dateList[6]).toISOString().split('T')[0],
  });

  useEffect(() => {
    setIsLoading(isSchedulesLoading);
  }, [isSchedulesLoading]);

  useEffect(() => {
    if (!schedules || isSchedulesLoading) return;
    setAllSchedules((prev) => removeDuplicate([...prev, ...schedules]));
  }, [schedules, isSchedulesLoading]);

  return (
    <div className='min-w-full'>
      <div className='grid w-full grid-cols-7 gap-4'>
        {dateList.map((date, index) => {
          return (
            <div
              key={index}
              className='flex flex-col gap-1'
              onClick={() => {
                setSelectedDate(new Date(date));
              }}
            >
              <CalenderDay dayIndex={index} />
              <CalendarDate
                date={new Date(date)}
                isSelected={date === selectedDate.valueOf()}
                haveSchedule={scheduleSet.has(
                  new Date(toKST(date)).toISOString().split('T')[0],
                )}
              />
            </div>
          );
        })}
      </div>
    </div>
  );
}

/**
 * 선택된 날짜 정보 컴포넌트 (요일, 월, 몇주차, 년도)
 * @param {object} props
 * @param {Date} props.date - 날짜
 * @param {boolean} props.isScrolling - 스크롤 중인지 여부
 * @returns {JSX.Element} - 선택된 날짜 정보 컴포넌트
 */
export function SelectedDateInfo({ date, isScrolling }) {
  const { weekDay, monthWeek, year } = getDateInfo(date);
  return (
    <div className='flex h-[80px] items-center justify-between'>
      <h1 className='text-[40px] font-semibold text-gray-100'>{`${weekDay}${isScrolling ? `, ${date.getDate().toString().padStart(2, '0')}` : ''}`}</h1>
      <div className='subtitle-2 flex flex-col text-end text-gray-200'>
        <p>{monthWeek}</p>
        <p>{year}</p>
      </div>
    </div>
  );
}

/**
 * 날짜 리스트 생성 함수
 * @param {object} props
 * @param {Date} props.curSunday - 현재 주의 일요일
 * @returns {Array} - 날짜 리스트
 */
function getDateList(curSunday) {
  const dateList = [];
  for (let i = 0; i < 7; i++) {
    dateList.push(
      new Date(curSunday).setDate(new Date(curSunday).getDate() + i),
    );
  }
  return dateList;
}

function removeDuplicate(schedules) {
  return [
    ...new Map(schedules.map((item) => [item.scheduleId, item])).values(),
  ];
}
