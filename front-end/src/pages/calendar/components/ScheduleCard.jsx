import Tag from '../../../components/Tag';
import { TagType } from '../../../components/Tag';
import Icon from '../../../components/Icon';
import MediumButton from '../../../components/buttons/MediumButton';
import { useEffect, useRef, useState } from 'react';
import classNames from 'classnames';
import { useUser } from '../../../useUser';

/**
 *
 * @param {string} teamName - 팀 이름
 * @param {object} schedule - 스케줄 정보
 * @param {array} roles - 팀원 역할 정보 배열
 * @param {function} onClickEdit - 수정 클릭 시 호출되는 함수
 * @returns
 */
export default function ScheduleCard({
  schedule,
  todos,
  onClickEdit,
  isFinished = false,
}) {
  const { userId } = useUser();
  const [isOpen, setIsOpen] = useState(false);
  const contentRef = useRef(null);
  const [height, setHeight] = useState(0);

  // 스케줄 컨텐츠 높이 계산
  useEffect(() => {
    if (contentRef.current) {
      setHeight(isOpen ? contentRef.current.scrollHeight : 0);
    }
  }, [isOpen]);

  // 나의 역할 찾기
  const myRole = todos.find((todo) => todo.memberId == userId)?.todoList;
  const teamRole = todos.filter((todo) => todo.memberId != userId);
  return (
    <div
      className={classNames(
        'rounded-400 flex h-fit w-full flex-col gap-6 overflow-hidden bg-gray-800 transition-all duration-300',
        schedule.teamName ? 'p-4' : 'px-4 py-5',
      )}
    >
      <div className='flex items-start'>
        <div className='flex flex-1 flex-col gap-3'>
          {schedule.teamName && (
            <Tag type={TagType.TEAM_NAME}>{schedule.teamName}</Tag>
          )}
          <div className='flex items-center gap-2'>
            <div className='subtitle-2 text-lime-500'>
              {schedule.startTime.split('T')[1].substring(0, 5)}
            </div>
            <hr className='h-4 w-1 bg-lime-500' />
            <p className='subtitle-2 text-gray-100'>{schedule.scheduleName}</p>
          </div>
        </div>
        {/* 일정 종료 전에만 수정 버튼 표시 */}
        {!isFinished && (
          <button onClick={() => onClickEdit()}>
            <Icon
              name='edit'
              className={classNames('h-max w-max', schedule.teamName && 'pt-1')}
            />
          </button>
        )}
      </div>
      <hr className='w-full border-gray-500' />
      {/* 나의 역할 */}
      {myRole.length > 0 ?
        <div className='flex flex-col gap-3'>
          <Tag type={TagType.MY_ROLE}></Tag>
          <div className='flex flex-col gap-1'>
            {myRole.map((role, index) => (
              <Role key={index}>{role}</Role>
            ))}
          </div>
        </div>
      : <div className='flex flex-col gap-6'>
          <p className='body-1 text-center text-gray-400'>
            나의 역할이 비어있어요
          </p>
        </div>
      }
      {/* 팀원 역할 */}
      <div
        ref={contentRef}
        className={`flex flex-col gap-6 overflow-hidden transition-all duration-300 ease-in-out ${isOpen ? 'mb-0' : 'mb-[-24px]'}`}
        style={contentRef.current ? { height: `${height}px` } : { height: 0 }}
      >
        {teamRole.length > 0 ?
          teamRole.map((todo, index) => {
            return (
              <div key={index} className='flex flex-col gap-3'>
                <Tag type={TagType.MEMBER_ROLE}>{todo.memberName}</Tag>
                {todo.todoList.length > 0 ?
                  <div className='flex flex-col gap-1'>
                    {todo.todoList.map((task, index) => (
                      <Role key={index}>{task}</Role>
                    ))}
                  </div>
                : <p className='body-1 text-gray-500'>
                    아직 담당 업무를 입력하지 않았어요
                  </p>
                }
              </div>
            );
          })
        : <div className='body-1 text-center text-gray-500'>
            표시할 팀원이 없어요
          </div>
        }
      </div>
      {/* 팀원 역할 보기 토글 버튼 */}
      <MediumButton
        text={
          <div className='flex items-center gap-2'>
            <Icon
              name={isOpen ? 'chevronUp' : 'chevronDown'}
              color='lime-500'
              className='h-max w-max'
            />
            {isOpen ? '닫기' : '팀원 역할 보기'}
          </div>
        }
        onClick={() => setIsOpen(!isOpen)}
        isOutlined={true}
        disabled={true}
      ></MediumButton>
    </div>
  );
}

/**
 * 역할 컴포넌트
 * @param {object} props
 * @param {string} props.children - 역할 텍스트
 * @returns {JSX.Element} - 역할 컴포넌트
 */
function Role({ children }) {
  return <p className='body-1 pl-1 text-gray-100'>{`• ${children}`}</p>;
}
