import classNames from 'classnames';
import Icon from '../../../components/Icon';
import { useEffect, useRef, useState } from 'react';
import {
  changeDayName,
  combineDateTime,
  getNearest10MinTime,
  toKST,
  toYMD,
} from '../../../utility/time';
import CustomInput from '../../../components/CustomInput';
import LargeButton from '../../../components/buttons/LargeButton';
import StickyWrapper from '../../../components/wrappers/StickyWrapper';
import { showToast } from '../../../utility/handleToast';
import {
  checkLength,
  checkNewSchedule,
  isEmpty,
} from '../../../utility/inputChecker';
import TimeSelector from './TimeSelector';
import Todo from './Todo';
import { hideModal, showModal } from '../../../utility/handleModal';
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
import { useUser } from '../../../useUser';

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
 * @param {function} props.setParentDate - 부모 컴포넌트의 날짜 설정 함수 : 메인페이지의 ScheduleAction인지, 일정페이지의 ScheduleAction인지 구분하는 용도로도 쓰임
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
  setAllSchedules,
  setParentDate = null,
}) {
  const nameLengthLimit = 20;
  const { selectedTeam } = useTeam();
  const { userId } = useUser();
  const scrollRef = useRef(null);
  const { selectedDate, setSelectedDate, clearData } = useScheduleAction(
    selectedDateFromParent,
    selectedScheduleFromParent,
  );

  const { mutate: postSchedule } = usePostSchedule(selectedTeam); // 일정 추가 API
  const { mutate: editSchedule } = useEditSchedule(selectedTeam); // 일정 수정 API
  const { mutate: deleteSchedule } = useDeleteSchedule({
    // 일정 삭제 API
    teamId: selectedTeam,
    scheduleStartTime: actionInfo.startTime,
  });
  const [dataReady, setDataReady] = useState(false);
  const [scheduleName, setScheduleName] = useState(actionInfo?.scheduleName); // 일정 제목 = 기존 제목으로 초기화
  const [startTime, setStartTime] = useState(actionInfo?.startTime); // 일정 시작일 = 기존 시작일로 초기화
  const [endTime, setEndTime] = useState(actionInfo?.endTime); // 일정 종료일 = 기존 종료일로 초기화
  const [todos, setTodos] = useState(actionInfo?.todos); // 역할 = 기존 역할로 초기화
  // 수정 가능 여부 = 팀 리더이거나, 일정 생성한 사람이거나, 일정 생성중이거나
  const canEdit =
    selectedScheduleFromParent?.leaderId === userId ||
    selectedScheduleFromParent?.ownerId === userId ||
    type === ScheduleActionType.ADD;

  // 일정 내용이 바뀐 경우 해당 내용 반영
  useEffect(() => {
    // setParentDate가 null일때 == 일정 화면에서 작업중일때
    if (!setParentDate) {
      // 해당 일정 내용을 반영
      setScheduleName(actionInfo?.scheduleName);
      setStartTime(actionInfo?.startTime);
      setEndTime(actionInfo?.endTime);
      setTodos(actionInfo?.todos);
    }
  }, [actionInfo]);

  // 추가/수정 바텀시트가 닫힐때 내용 초기화
  useEffect(() => {
    // 닫혀있고 setParentDate가 null이 아닐때 == 메인 페이지에서 창 닫을때
    if (!isOpen && setParentDate) {
      // 내용 초기화
      setScheduleName(null);
      setStartTime(getNearest10MinTime(new Date()));
      setEndTime(getNearest10MinTime(new Date().valueOf() + 1000 * 60 * 10));
      setTodos([]);
    }
  }, [isOpen]);

  // 데이터 준비 되면 전송
  useEffect(() => {
    if (dataReady && checkNewSchedule(scheduleName, startTime, endTime)) {
      const sendingData = {
        name: scheduleName,
        startTime: toKST(startTime).toISOString(),
        endTime: toKST(endTime).toISOString(),
        todos: todos,
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

  // 완료 버튼 누르면 데이터 적절히 다듬어서 전송할 데이터 세팅 시키기
  const handleSubmitButton = () => {
    // 빈 투두 없애기
    const newTodos = todos.filter((todo) => !isEmpty(todo));
    setTodos(newTodos);
    if (!dateFixed) {
      // 선택한 날짜와 선택한 시간 적용
      if (toYMD(selectedDate) !== toYMD(startTime)) {
        setStartTime(combineDateTime(selectedDate, startTime));
        setEndTime(combineDateTime(selectedDate, endTime));
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
              setAllSchedules((prev) => [
                ...prev.filter(
                  (schedule) =>
                    schedule.scheduleId !==
                    selectedScheduleFromParent.scheduleId,
                ),
              ]);
              showToast('일정을 삭제했습니다');
              onClose();
            },
          });
        }}
        onClose={onClose}
      />,
    );

  const handleCloseActionButton = () => {
    if (type !== ScheduleActionType.EDIT) {
      clearData();
    }
    onClose();
  };

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
        {canEdit && type === ScheduleActionType.EDIT && (
          <button onClick={handleDeleteButton}>
            <Icon name='remove' className='absolute top-5 left-0 text-white' />
          </button>
        )}
        <button onClick={handleCloseActionButton}>
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
              if (canEdit) {
                setSelectedDate(newDate);
                if (setParentDate) {
                  setParentDate(newDate);
                }
              } else {
                showToast('일정 정보는 팀장 또는 일정 생성자만 변경 가능해요');
              }
            }}
            customInput={<DatePickerButton />}
          />
          <div className='h-11 shrink-0' />
        </>
      }

      <CustomInput
        label='일정 이름'
        content={scheduleName ?? ''}
        setContent={
          canEdit ?
            (text) => {
              const newName = checkLength(text, nameLengthLimit);
              setScheduleName(newName);
            }
          : null
        }
        isOutlined={false}
        bgColor='gray-700'
      />
      <div className='h-11 shrink-0' />

      <TimeSelector
        startTime={startTime ?? selectedDate}
        setStartTime={(time) => {
          if (canEdit) {
            setStartTime(time);
          } else {
            showToast('일정 정보는 팀장 또는 일정 생성자만 변경 가능해요');
          }
        }}
        endTime={endTime ?? selectedDate}
        setEndTime={(time) => {
          if (canEdit) {
            setEndTime(time);
          } else {
            showToast('일정 정보는 팀장 또는 일정 생성자만 변경 가능해요');
          }
        }}
      />

      <div className='h-11 shrink-0' />

      <Todo todos={todos ?? []} setTodo={setTodos} scrollRef={scrollRef} />

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
