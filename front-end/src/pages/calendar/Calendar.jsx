import { useEffect, useState } from 'react';
import { SelectedDateInfo } from './components/CalendarParts';
import CalendarWeeks from './components/CalendarWeeks';
import Accordion from '../../components/Accordion';
import ScheduleCard from './components/ScheduleCard';
import StickyWrapper from '../../components/wrappers/StickyWrapper';
import LargeButton from '../../components/buttons/LargeButton';
import Icon from '../../components/Icon';
import { checkIsFinished } from '../../utility/time';
import { ScheduleActionType } from './components/ScheduleAction';
import ScheduleAction from './components/ScheduleAction';
import { useLocation } from 'react-router-dom';
import useSchedule from './hooks/useSchedule';
import useScheduleAction from './hooks/useScheduleAction';
import useCalendarScroll from './hooks/useCalendarScroll';
import { useTeam } from '../../useTeam';

export default function Calendar() {
  const location = useLocation();
  // 팀 불러오기
  const { teams, selectedTeam, selectTeam } = useTeam();
  const [showAllSchedule, setShowAllSchedule] = useState(false);

  // 날짜 지정
  const [selectedDate, setSelectedDate] = useState(
    location.state?.scheduleDate ?? new Date(new Date().setHours(0, 0, 0, 0)),
  );

  // 일정 조회 관련
  const {
    setAllSchedules,
    scheduleOnDate,
    scheduleSet,
    selectedSchedule,
    setSelectedSchedule,
  } = useSchedule(selectedTeam, selectedDate, showAllSchedule);

  // 일정 등록, 수정, 삭제 등 액션 관련
  const {
    doingAction,
    setDoingAction,
    actionType,
    setActionType,
    clearData,
    actionInfo,
  } = useScheduleAction(selectedDate, selectedSchedule);

  // 일정 화면 스크롤 관련
  const { scrollRef, isScrolling } = useCalendarScroll();

  return (
    <div className='relative overflow-y-hidden'>
      <div
        ref={scrollRef}
        className='scrollbar-hidden h-dvh overflow-x-hidden overflow-y-auto px-5'
      >
        <StickyWrapper>
          <Accordion
            isMainPage={false}
            selectedTeamId={selectedTeam}
            teamList={teams}
            onTeamClick={(teamId) => {
              selectTeam(teamId);
              setShowAllSchedule(false);
            }}
            canClose={!doingAction}
            onClickLastButton={() => {
              setShowAllSchedule(true);
            }}
            showAllSchedule={showAllSchedule}
          />
          <SelectedDateInfo date={selectedDate} isScrolling={isScrolling} />
        </StickyWrapper>
        <CalendarWeeks
          selectedDate={selectedDate}
          setSelectedDate={setSelectedDate}
          scheduleSet={scheduleSet}
          setAllSchedules={setAllSchedules}
        />
        <ul className='flex flex-col gap-6'>
          {scheduleOnDate &&
            scheduleOnDate.map((schedule, index) => {
              if (!showAllSchedule && schedule.teamId !== selectedTeam)
                return null;
              return (
                <li key={index} className='last:mb-5'>
                  <ScheduleCard
                    schedule={schedule}
                    todos={schedule.scheduleMemberNestedDtoList}
                    isFinished={checkIsFinished(schedule.endTime)}
                    onClickEdit={() => {
                      setSelectedSchedule(schedule);
                      setActionType(ScheduleActionType.EDIT);
                      setDoingAction(true);
                    }}
                  />
                </li>
              );
            })}
          <li className='mb-5'>
            <LargeButton
              text={
                <p className='button-1 flex items-center gap-2 text-gray-300'>
                  <Icon name='plusS' />
                  새로운 일정 추가
                </p>
              }
              onClick={() => {
                clearData();
                setActionType(ScheduleActionType.ADD);
                setDoingAction(true);
              }}
              isOutlined={true}
              disabled={true}
            />
          </li>
        </ul>
      </div>
      {scheduleOnDate && (
        <ScheduleAction
          type={actionType}
          isOpen={doingAction}
          selectedDateFromParent={selectedDate}
          selectedScheduleFromParent={selectedSchedule}
          onClose={() => setDoingAction(false)}
          actionInfo={actionInfo}
          dateFixed={actionType === ScheduleActionType.ADD}
          setAllSchedules={setAllSchedules}
          setParentDate={setSelectedDate}
        />
      )}
    </div>
  );
}
