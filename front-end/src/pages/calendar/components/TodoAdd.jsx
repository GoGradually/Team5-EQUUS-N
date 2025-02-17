import classNames from 'classnames';
import Icon from '../../../components/Icon';
import { useEffect, useRef, useState } from 'react';
import LargeButton from '../../../components/buttons/LargeButton';
import StickyWrapper from '../../../components/wrappers/StickyWrapper';
import { showToast } from '../../../utility/handleToast';
import { isEmpty } from '../../../utility/inputChecker';
import Todo from './Todo';
import { useUser } from '../../../useUser';
import { useEditSchedule } from '../../../api/useCalendar';

/**
 * 일정 추가 페이지
 * @param {object} props
 * @param {boolean} props.isOpen - 페이지 열림 여부
 * @param {function} props.onClose - 페이지 닫기 함수
 * @param {function} props.onSubmit - 일정 추가 완료 함수
 * @param {Date} props.selectedDate - 선택된 날짜
 */
export default function TodoAdd({ isOpen, onClose, selectedSchedule }) {
  const { userId } = useUser();
  const [todos, setTodo] = useState(
    selectedSchedule?.scheduleMemberNestedDtoList?.find((dto) => {
      return dto.memberId == userId;
    })?.todoList ?? [],
  );
  const scrollRef = useRef(null);

  const { mutate: editSchedule } = useEditSchedule(selectedSchedule.teamId);

  useEffect(() => {
    const newTodos =
      selectedSchedule?.scheduleMemberNestedDtoList?.find((dto) => {
        return dto.memberId == userId;
      })?.todoList ?? [];
    setTodo(newTodos);
  }, [selectedSchedule]);

  const handleSubmitButton = () => {
    const newTodos = todos.filter((todo) => !isEmpty(todo));
    const editedSchedule = {
      name: selectedSchedule.scheduleName,
      startTime: selectedSchedule.startTime,
      endTime: selectedSchedule.endTime,
      todos: newTodos,
    };
    editSchedule(
      {
        scheduleId: selectedSchedule.scheduleId,
        data: editedSchedule,
      },
      {
        onSuccess: () => {
          showToast('역할이 추가되었어요');
          setTodo([]);
          onClose();
        },
      },
    );
  };

  return (
    <div
      ref={scrollRef}
      className={classNames(
        'rounded-t-400 absolute z-1000 flex h-[calc(100%-60px)] w-full max-w-[430px] flex-col overflow-y-auto bg-gray-800 px-5 transition-all duration-300',
        isOpen ? 'visible bottom-0' : 'invisible -bottom-[100%]',
      )}
    >
      <StickyWrapper bgColor='gray-800'>
        <h1 className='subtitle-1 pt-5 text-center text-white'>
          내 역할 추가하기
        </h1>
        <button
          onClick={() => {
            setTodo([]);
            onClose();
          }}
        >
          <Icon name='delete' className='absolute top-5 right-0 text-white' />
        </button>
      </StickyWrapper>

      <div className='h-8 shrink-0' />

      <div className='flex items-center gap-2'>
        <hr className='h-6 w-1.5 rounded-[2px] bg-lime-500' />
        <h2 className='header-3 text-white'>
          {`${selectedSchedule?.scheduleName ?? '일정 제목 없음'}`}
        </h2>
      </div>
      <div className='h-3 shrink-0' />

      <Todo todos={todos} setTodo={setTodo} scrollRef={scrollRef} />

      <div className='h-11 shrink-0' />
      <div className='flex-1' />

      <div
        className={`relative bottom-0 mb-5 max-w-[calc(390px)] transition-all duration-300`}
      >
        <LargeButton
          isOutlined={false}
          text={'완료'}
          onClick={handleSubmitButton}
        />
      </div>
    </div>
  );
}
