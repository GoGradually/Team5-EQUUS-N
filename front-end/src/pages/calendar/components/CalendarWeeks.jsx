import { useState, useRef, useEffect } from 'react';
import { getRecentSunday } from '../../../utility/time';
import { CalendarWeek, SelectedDateInfo } from './CalendarParts';
import classNames from 'classnames';

/**
 * 캘린더 컴포넌트
 * @param {object} props
 * @param {Date} props.selectedDate - 선택된 날짜
 * @param {function} props.setSelectedDate - 선택된 날짜 설정 함수
 * @param {Array} props.scheduleSet - 일정 데이터
 * @param {function} props.setAllSchedules - 전체 일정 설정 함수
 * @param {boolean} props.setIsLoading - 로딩 상태 설정 함수
 * @returns {JSX.Element} - 캘린더 컴포넌트
 */
export default function CalendarWeeks({
  selectedDate,
  setSelectedDate,
  scheduleSet,
  setAllSchedules,
  setIsLoading,
}) {
  const [curSunday, setCurSunday] = useState(getRecentSunday(selectedDate));
  const [isDragging, setIsDragging] = useState(false);
  const startX = useRef(0);
  const scrollLeft = useRef(0);
  const containerRef = useRef(null);
  const dragSensitivity = 1.7;

  // 현재 주를 가운데에 배치
  useEffect(() => {
    if (!containerRef.current) return;

    const container = containerRef.current;
    container.scrollLeft = container.clientWidth;
  }, []);

  // 주가 저번주나 다음주로 넘어가면 캘린더 플립
  useEffect(() => {
    if (!containerRef.current) return;

    const container = containerRef.current;
    const handleScroll = () => {
      const containerLeft = container.scrollLeft;
      if (containerLeft === 0) {
        flipCalender(container, false);
      } else if (
        Math.round(containerLeft) === Math.round(container.clientWidth * 2)
      ) {
        flipCalender(container, true);
      }
    };

    container.addEventListener('scroll', handleScroll);
    return () => container.removeEventListener('scroll', handleScroll);
  }, [curSunday]);

  // 드래그 시작 - 시작 위치 저장
  const handleDragStart = (e) => {
    e.preventDefault(); // 이벤트 기본 동작 방지
    setIsDragging(true);
    startX.current =
      (e.clientX || e.touches[0].clientX) - containerRef.current.offsetLeft;
    scrollLeft.current = containerRef.current.scrollLeft;
  };

  // 드래그 중 - 드래그 거리에 따라 스크롤 이동
  const handleDrag = (e) => {
    if (!isDragging) return;
    const x =
      (e.clientX || e.touches[0].clientX) - containerRef.current.offsetLeft;
    const walk = (x - startX.current) * dragSensitivity;
    containerRef.current.scrollLeft = scrollLeft.current - walk;
  };

  // 드래그 끝 - 스크롤 위치에 따라 가장 가까운 주로 스냅
  const handleDragEnd = () => {
    setIsDragging(false);
    const container = containerRef.current;
    const weekWidth = container.clientWidth;
    const targetScroll =
      Math.round(container.scrollLeft / weekWidth) * weekWidth;

    container.scrollTo({
      left: targetScroll,
      behavior: 'smooth',
    });
  };

  // 캘린더 넘기기
  const flipCalender = (container, isNext) => {
    // 캘린더가 변하는 동안 투명하게 만듦
    requestAnimationFrame(() => {
      container.style.opacity = '0';

      // 모든 상태 업데이트를 다음 프레임으로 지연
      setTimeout(() => {
        handleDragEnd();
        // 새로운 주 계산
        const newSunday = new Date(curSunday);
        newSunday.setDate(newSunday.getDate() + (isNext ? 7 : -7));
        const newDate = new Date(selectedDate);
        newDate.setDate(newDate.getDate() + (isNext ? 7 : -7));

        // 변경된 주를 현재 주로 설정, 선택된 날짜도 변경
        Promise.all([setCurSunday(newSunday), setSelectedDate(newDate)]).then(
          () => {
            container.scrollLeft = container.clientWidth;
            // 모든 작업이 완료된 후 opacity 복구
            requestAnimationFrame(() => {
              container.style.opacity = '1';
            });
          },
        );
      }, 0);
    });
  };

  return (
    <div
      ref={containerRef}
      className={classNames(
        'flex w-full overflow-hidden select-none',
        isDragging && 'cursor-grabbing',
        !isDragging && 'cursor-grab',
      )}
      onMouseDown={handleDragStart}
      onMouseMove={handleDrag}
      onMouseUp={handleDragEnd}
      onMouseLeave={handleDragEnd}
      onTouchEnd={handleDragEnd}
      onTouchStart={handleDragStart}
      onTouchMove={handleDrag}
      onTouchCancel={handleDragEnd}
    >
      {/* 저번주 */}
      <div className='min-w-full py-4'>
        <CalendarWeek
          curSunday={new Date(curSunday).setDate(curSunday.getDate() - 7)}
          selectedDate={selectedDate}
          setSelectedDate={setSelectedDate}
          scheduleSet={scheduleSet}
          setAllSchedules={setAllSchedules}
          setIsLoading={setIsLoading}
        />
      </div>
      {/* 이번주 */}
      <div className='min-w-full py-4'>
        <CalendarWeek
          curSunday={new Date(curSunday)}
          selectedDate={selectedDate}
          setSelectedDate={setSelectedDate}
          scheduleSet={scheduleSet}
          setAllSchedules={setAllSchedules}
          setIsLoading={setIsLoading}
        />
      </div>
      {/* 다음주 */}
      <div className='min-w-full py-4'>
        <CalendarWeek
          curSunday={new Date(curSunday).setDate(curSunday.getDate() + 7)}
          selectedDate={selectedDate}
          setSelectedDate={setSelectedDate}
          scheduleSet={scheduleSet}
          setAllSchedules={setAllSchedules}
          setIsLoading={setIsLoading}
        />
      </div>
    </div>
  );
}
