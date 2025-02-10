import { useState } from 'react';
import Icon from '../../../components/Icon';
import ProfileImage from '../../../components/ProfileImage';
import Tag from '../../../components/Tag';
import {
  useFeedbackLikeCancel,
  useFeedbackLike,
} from '../../../api/useFeedback';

export const FeedBackType = Object.freeze({
  SELF: 'SELF',
  RECEIVE: 'FROM. ',
  SEND: 'TO. ',
});

/**
 * 피드백 컴포넌트
 * @param {object} props
 * @param {string} props.feedbackType - 피드백 타입
 * @param {object} props.data - 피드백 데이터
 * @returns {JSX.Element} - 피드백 컴포넌트
 */
export default function FeedBack({ feedbackType, data }) {
  const teamMate = feedbackType === 'RECEIVE' ? data.sender : data.receiver;
  const date = data.createdAt.split('T')[0].replace(/-/g, '.');

  const { mutate: likeFeedback } = useFeedbackLike(1, data.feedbackId);
  const { mutate: cancelLikeFeedback } = useFeedbackLikeCancel(
    1,
    data.feedbackId,
  );
  const [isLiked, setIsLiked] = useState(data.liked);

  return (
    <div className='flex flex-col gap-5 border-b-8 border-gray-800 bg-gray-900 py-5'>
      <div className='flex gap-3'>
        {/* 회고 아닌 경우에만 프로필 이미지 표시 */}
        {FeedBackType[feedbackType] !== FeedBackType.SELF && (
          <div className='aspect-square h-11'>
            <ProfileImage
              iconName={'@animals/' + teamMate.image}
              color={teamMate.backgroundColor}
            />
          </div>
        )}
        <div className='flex flex-1 flex-col gap-0.5'>
          {/* 회고 아닌 경우 To. 또는 From. + 이름 표시 || 회고인 경우 팀 이름 표시 */}
          <p className='body-3 text-gray-0'>
            {FeedBackType[feedbackType] !== FeedBackType.SELF ?
              FeedBackType[feedbackType] +
              ((
                FeedBackType[feedbackType] === FeedBackType.RECEIVE &&
                data.isAnonymous
              ) ?
                '익명'
              : teamMate.name)
            : data.title}
          </p>
          {/* 회고 아닌 경우 팀 이름 표시 || 회고인 경우 일정 표시 */}
          <p className='caption-1 text-gray-300'>
            {FeedBackType[feedbackType] !== FeedBackType.SELF ?
              data.teamName
            : data.teamName}
          </p>
        </div>
        <div className='flex flex-col justify-between'>
          {(
            FeedBackType[feedbackType] === FeedBackType.SEND && data.isAnonymous
          ) ?
            <Tag type='MEMBER_ROLE'>익명으로 보냄</Tag>
          : <div></div>}
          {/* 회고인 경우 날짜 상단에 표시 */}
          {FeedBackType[feedbackType] === FeedBackType.SELF ?
            <p className='caption-1 text-gray-300'>{date}</p>
          : <div></div>}
          {/* 익명으로 보낸 경우 익명태그 표시 */}
        </div>
      </div>
      <p className='body-1 text-gray-0'>
        {FeedBackType[feedbackType] === FeedBackType.SELF ?
          data.content
        : data.subjectiveFeedback}
      </p>
      {/* 회고 아닌 경우 키워드, 날짜, 하트 표시 */}
      {FeedBackType[feedbackType] !== FeedBackType.SELF && (
        <>
          <div className='flex flex-wrap gap-2'>
            {data.objectiveFeedbacks.map((keyword, i) => {
              return (
                <Tag key={i} type='KEYWORD'>
                  {keyword}
                </Tag>
              );
            })}
          </div>
          <div className='flex items-center justify-between'>
            <p className='caption-1 text-gray-300'>{date}</p>
            {FeedBackType[feedbackType] === FeedBackType.RECEIVE &&
              // 받은 피드백의 경우 하트 토글 가능
              (isLiked ?
                <button
                  onClick={() => {
                    setIsLiked(false);
                    cancelLikeFeedback();
                  }}
                >
                  <Icon name='heartFill' />
                </button>
              : <button
                  onClick={() => {
                    setIsLiked(true);
                    likeFeedback();
                  }}
                >
                  <Icon name='heartDefault' />
                </button>)}
            {FeedBackType[feedbackType] === FeedBackType.SEND && isLiked && (
              //보낸 피드백의 경우 하트 받았는지 여부만 표시
              <div className='caption-1 flex items-center gap-1 text-gray-300'>
                {teamMate.name}님에게 도움이 됐어요!
                <Icon name='heartFill' className={'scale-75'}></Icon>
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}
