import { forwardRef } from 'react';
import DatePicker from 'react-datepicker';
import Icon from './Icon';
import { changeDayName } from '../utility/time';
import CustomInput from './CustomInput';

export default function CustomDatePicker({
  dateFormat = 'MM/dd',
  date,
  setDate,
  customInput,
  setIsPickerOpen = () => {},
}) {
  return (
    <div className='relative w-full'>
      <DatePicker
        dateFormat={dateFormat}
        selected={date}
        onChange={(date) => setDate(date)}
        customInput={customInput}
        onCalendarOpen={() => setIsPickerOpen(true)}
        onCalendarClose={() => setIsPickerOpen(false)}
        dayClassName={(d) => {
          d.getDate() === date?.getDate() ? 'bg-red-500' : 'bg-lime-500';
        }}
        formatWeekDay={(nameOfDay) => {
          return changeDayName(nameOfDay.substring(0, 3));
        }}
        // withPortal
        popperPlacement='bottom-end'
      />
    </div>
  );
}

export const DatePickerDropdown = forwardRef(function DatePickerDropdown(
  { value, isStartTime, onClick, isPickerOpen },
  ref,
) {
  return (
    <div ref={ref} className='flex-1' onClick={onClick}>
      <div
        className={`rounded-200 body-1 text-gray-0 flex cursor-pointer items-center justify-between border px-5 py-3.5 ${isPickerOpen ? 'border-gray-300' : 'border-gray-600'}`}
      >
        <p className='text-center overflow-ellipsis'>
          {value + (isStartTime ? ' 부터' : ' 까지')}
        </p>
        <Icon
          name='chevronDown'
          className={`ml-0.5 transition ${isPickerOpen ? 'rotate-180' : ''}`}
        />
      </div>
    </div>
  );
});

export const DatePickerButton = forwardRef(function DatePickerButton(
  { value, onClick },
  ref,
) {
  let selectedDay = value.split(' ')[1];
  selectedDay = changeDayName(selectedDay);
  const output = value.split(' ')[0] + ' ' + selectedDay + '요일';
  return (
    <div ref={ref} onClick={onClick}>
      <CustomInput
        label={'날짜'}
        content={output}
        isOutlined={false}
        bgColor='gray-700'
        addOn={
          <button onClick={onClick}>
            <Icon name='calendar' />
          </button>
        }
      />
    </div>
  );
});
