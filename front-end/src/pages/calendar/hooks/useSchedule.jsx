import { useEffect, useState } from 'react';
import { timeInPeriod, toKST } from '../../../utility/time';

export default function useSchedule(teamId, selectedDate, showAllSchedule) {
  const [allSchedules, setAllSchedules] = useState([]);
  const [scheduleOnDate, setScheduleOnDate] = useState(null);
  const [scheduleSet, setScheduleSet] = useState(new Set());
  const [selectedSchedule, setSelectedSchedule] = useState(
    scheduleOnDate ? scheduleOnDate[0] : null,
  );

  // 해당 날짜에 있는 스케줄 필터링
  useEffect(() => {
    if (!allSchedules) return;
    setScheduleOnDate(
      allSchedules.filter((data) => {
        return timeInPeriod(
          new Date(data.startTime),
          selectedDate,
          new Date(new Date(selectedDate).getTime() + 86400000),
        );
      }),
    );
  }, [allSchedules, selectedDate]);

  // 특정 팀 관련 일정들의 날짜 모두 종합
  useEffect(() => {
    setScheduleSet(
      new Set(
        allSchedules
          ?.filter((data) => {
            if (showAllSchedule) return true;
            return data.teamId === teamId;
          })
          ?.map(
            (data) =>
              new Date(toKST(data.startTime)).toISOString().split('T')[0],
          ) ?? [],
      ),
    );
  }, [allSchedules, teamId, showAllSchedule]);

  return {
    allSchedules,
    setAllSchedules,
    scheduleOnDate,
    scheduleSet,
    selectedSchedule,
    setSelectedSchedule,
  };
}
