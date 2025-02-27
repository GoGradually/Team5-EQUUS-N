import { useEffect, useState } from 'react';
import { useMembers } from '../../../api/useTeamspace';
import ProfileImage from '../../../components/ProfileImage';

/**
 * 팀 요소 컴포넌트
 * @param {object} props
 * @param {string} props.teamName - 팀 이름
 * @param {string} props.startDate - 팀 시작 날짜
 * @param {string} props.endDate - 팀 종료 날짜
 * @param {object[]} props.teamMembers - 팀원 목록
 * @param {string} props.teamMembers[].iconName - 팀원 아이콘 이름
 * @param {string} props.teamMembers[].color - 팀원 아이콘 색상
 * @param {boolean} props.isDeleted - 팀 삭제 여부
 * @returns {JSX.Element} - 팀 요소 컴포넌트
 */
export default function TeamElement({
  teamId,
  teamName,
  startDate,
  endDate,
  isEnded,
}) {
  const { data: members } = useMembers(teamId);
  const [teamMembers, setTeamMembers] = useState([]);

  useEffect(() => {
    if (members) {
      setTeamMembers(members);
    }
  }, [members]);

  const imageGap = 20;
  const zIndexGap = 10;
  const maxShow = 6;
  return (
    <li
      className={`rounded-400 flex h-fit w-full flex-col items-center justify-between gap-4 bg-gray-800 p-5 ${
        isEnded ? 'opacity-60' : ''
      }`}
    >
      <div className='relative flex w-full justify-between'>
        <p className='subtitle-1 relative flex-1 text-start text-gray-100'>
          {teamName}
        </p>
        {teamMembers.map((member, index) => {
          if (index <= maxShow) {
            return (
              <div
                key={index}
                className='absolute top-0'
                style={{
                  right: index === maxShow ? `0px` : `${index * imageGap}px`,
                  zIndex:
                    index === maxShow ?
                      Math.floor(zIndexGap / 2)
                    : index * zIndexGap,
                }}
              >
                {index === maxShow ?
                  <div className='h-8 w-8'>
                    <ProfileImage
                      iconName={'dots'}
                      color={'var(--color-gray-200)'}
                    />
                  </div>
                : <div className='h-8 w-8'>
                    <ProfileImage
                      iconName={`@animals/${member.profileImage.image}`}
                      color={member.profileImage.backgroundColor}
                    />
                  </div>
                }
              </div>
            );
          } else {
            return null;
          }
        })}
      </div>
      <div className='caption-1 flex w-full justify-between text-gray-300'>
        <p>
          {startDate} ~ {endDate}
        </p>
        <p>{teamMembers.length}명</p>
      </div>
    </li>
  );
}
