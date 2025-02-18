import classNames from 'classnames';
import Icon from '../../../components/Icon';
import { useEffect, useRef, useState } from 'react';
import {
  changeDayName,
  combineDateTime,
  toKST,
  toYMD,
} from '../../../utility/time';
import CustomInput from '../../../components/CustomInput';
import LargeButton from '../../../components/buttons/LargeButton';
import StickyWrapper from '../../../components/wrappers/StickyWrapper';
import { showToast } from '../../../utility/handleToast';
import { checkNewSchedule, isEmpty } from '../../../utility/inputChecker';
import TimeSelector from './TimeSelector';
import Todo from './Todo';
import { showModal } from '../../../utility/handleModal';
import ScheduleDeleteModal from './ScheduleDeleteModal';
import CustomDatePicker, {
  DatePickerButton,
} from '../../../components/CustomDatePicker';
import {
  useDeleteSchedule,
  useEditSchedule,
  usePostSchedule,
} from '../../../api/useCalendar';
import { useTeam } from '../../../useTeam';
import useScheduleAction from '../hooks/useScheduleAction';

export const ScheduleActionType = Object.freeze({
  ADD: 'add',
  EDIT: 'edit',
});

/**
 * 일정 추가 페이지
 * @param {object} props
 * @param {string} props.type - 페이지 타입
 * @param {boolean} props.isOpen - 페이지 열림 여부
 * @param {function} props.onClose - 페이지 닫기 함수
 * @param {Date} props.selectedDateFromParent - 선택된 날짜
 * @param {object} props.selectedScheduleFromParent - 선택된 일정
 * @param {object} props.actionInfo - 일정 정보
 * @param {boolean} props.dateFixed - 날짜 고정 여부
 * @param {function} props.setParentDate - 부모 컴포넌트의 날짜 설정 함수
 * @returns {JSX.Element}
 */
export default function ScheduleAction({
  type,
  isOpen,
  onClose,
  selectedScheduleFromParent,
  selectedDateFromParent,
  actionInfo,
  dateFixed,
  setParentDate = () => {},
}) {
  const { selectedTeam } = useTeam();
  const scrollRef = useRef(null);
  const { selectedDate, setSelectedDate, clearData } = useScheduleAction(
    selectedDateFromParent,
    selectedScheduleFromParent,
  );

  const { mutate: postSchedule } = usePostSchedule(selectedTeam);
  const { mutate: editSchedule } = useEditSchedule(selectedTeam);
  const { mutate: deleteSchedule } = useDeleteSchedule(selectedTeam);
  const [dataReady, setDataReady] = useState(false);

  useEffect(() => {
    if (
      dataReady &&
      checkNewSchedule(
        actionInfo.scheduleName,
        actionInfo.startTime,
        actionInfo.endTime,
      )
    ) {
      const sendingData = {
        name: actionInfo.scheduleName,
        startTime: toKST(actionInfo.startTime).toISOString(),
        endTime: toKST(actionInfo.endTime).toISOString(),
        todos: actionInfo.todos,
      };
      if (type === ScheduleActionType.ADD) {
        postSchedule(sendingData, {
          onSuccess: () => {
            onClose();
            showToast('일정이 추가되었어요');
            clearData();
          },
        });
        setDataReady(false);
      } else {
        editSchedule(
          {
            scheduleId: selectedScheduleFromParent.scheduleId ?? -1,
            data: sendingData,
          },
          {
            onSuccess: () => {
              onClose();
              showToast('일정이 수정되었어요');
              clearData();
            },
          },
        );
        setDataReady(false);
      }
    } else {
      setDataReady(false);
    }
  }, [dataReady]);

  const handleSubmitButton = () => {
    const newTodos = actionInfo.todos.filter((todo) => !isEmpty(todo));
    actionInfo.setTodo(newTodos);
    if (!dateFixed) {
      if (toYMD(selectedDate) !== toYMD(actionInfo.startTime)) {
        actionInfo.setStartTime(
          combineDateTime(selectedDate, actionInfo.startTime),
        );
        actionInfo.setEndTime(
          combineDateTime(selectedDate, actionInfo.endTime),
        );
      }
    }
    setDataReady(true);
  };

  const handleDeleteButton = () =>
    showModal(
      <ScheduleDeleteModal
        deleteSchedule={() => {
          deleteSchedule(selectedScheduleFromParent.scheduleId ?? -1, {
            onSuccess: () => {
              showToast('일정을 삭제했습니다');
              hideModal();
              onClose();
            },
          });
        }}
        onClose={onClose}
      />,
    );

  return (
    <div
      ref={scrollRef}
      className={classNames(
        'rounded-t-400 absolute z-1000 flex h-[calc(100%-60px)] w-full max-w-[430px] flex-col overflow-x-hidden overflow-y-auto bg-gray-800 px-5 transition-all duration-300',
        isOpen ? 'visible bottom-0' : 'invisible -bottom-[100%]',
      )}
    >
      <StickyWrapper bgColor='gray-800'>
        <h1 className='subtitle-1 pt-5 text-center text-white'>
          {type === ScheduleActionType.ADD ? '일정 추가하기' : '일정 수정하기'}
        </h1>
        {type === ScheduleActionType.EDIT && (
          <button onClick={handleDeleteButton}>
            <Icon name='remove' className='absolute top-5 left-0 text-white' />
          </button>
        )}
        <button
          onClick={() => {
            if (type !== ScheduleActionType.EDIT) {
              clearData();
            }
            onClose();
          }}
        >
          <Icon name='delete' className='absolute top-5 right-0 text-white' />
        </button>
      </StickyWrapper>

      <div className='h-8 shrink-0' />

      {dateFixed ?
        <>
          <div className='flex items-center gap-2'>
            <hr className='h-6 w-1.5 rounded-[2px] bg-lime-500' />
            <h2 className='header-3 text-white'>
              {`${selectedDateFromParent.getMonth() + 1}월 ${selectedDateFromParent.getDate()}일, ${changeDayName(selectedDateFromParent.getDay()) + '요일'}`}
            </h2>
          </div>
          <div className='h-3 shrink-0' />
        </>
      : <>
          <CustomDatePicker
            dateFormat='yyyy-MM-dd eee'
            date={selectedDate}
            setDate={(newDate) => {
              setSelectedDate(newDate);
              setParentDate(newDate);
            }}
            customInput={<DatePickerButton />}
          />
          <div className='h-11 shrink-0' />
        </>
      }

      <CustomInput
        label='일정 이름'
        content={actionInfo?.scheduleName ?? ''}
        setContent={actionInfo?.setScheduleName ?? ''}
        isOutlined={false}
        bgColor='gray-700'
      />
      <div className='h-11 shrink-0' />

      <TimeSelector
        startTime={actionInfo?.startTime ?? selectedDate}
        setStartTime={actionInfo?.setStartTime}
        endTime={actionInfo?.endTime ?? selectedDate}
        setEndTime={actionInfo?.setEndTime}
      />

      <div className='h-11 shrink-0' />

      <Todo
        todos={actionInfo?.todos ?? []}
        setTodo={actionInfo?.setTodo}
        scrollRef={scrollRef}
      />

      <div className='h-11 shrink-0' />
      <div className='flex-1' />

      <div
        className={`relative bottom-0 mb-5 max-w-[calc(390px)] transition-all duration-300`}
      >
        <LargeButton
          isOutlined={false}
          text={type === ScheduleActionType.ADD ? '추가 완료' : '수정 완료'}
          onClick={handleSubmitButton}
        />
      </div>
    </div>
  );
}
