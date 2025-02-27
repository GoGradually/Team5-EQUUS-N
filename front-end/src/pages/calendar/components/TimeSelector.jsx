import { DropdownLarge } from '../../../components/Dropdown';
import { timeOptions } from '../../../utility/time';

export default function TimeSelector({
  startTime,
  setStartTime,
  endTime,
  setEndTime,
}) {
  const startText = new Date(startTime)
    .toTimeString()
    .split(' ')[0]
    .slice(0, 5);
  const endText = new Date(endTime).toTimeString().split(' ')[0].slice(0, 5);
  return (
    <div className='flex flex-col gap-2'>
      <h3 className='subtitle-2 text-gray-0'>일정 시간</h3>
      <div className='flex items-center gap-3'>
        <DropdownLarge
          triggerText={startText}
          setTriggerText={(text) =>
            setStartTime(timeStringToDate(startTime, text))
          }
          isFromTime={true}
          items={timeOptions}
        />
        <DropdownLarge
          triggerText={endText}
          setTriggerText={(text) => setEndTime(timeStringToDate(endTime, text))}
          isFromTime={false}
          items={timeOptions}
        />
      </div>
    </div>
  );
}

function timeStringToDate(selectedDate, timeString) {
  const [hours, minutes] = timeString.split(':').map(Number); // "02:10" → [2, 10]
  const date = new Date(selectedDate); // 현재 날짜
  date.setHours(hours, minutes, 0, 0); // 시, 분, 초, 밀리초 설정
  return date;
}
