import { useLocation, useNavigate, useParams } from 'react-router-dom';
import NavBar from '../../components/NavBar';
import Tag, { TagType } from '../../components/Tag';
import StickyWrapper from '../../components/wrappers/StickyWrapper';
import { useInviteTeam, useTeamInfo } from '../../api/useTeamspace';
import { useEffect, useState } from 'react';
import Icon from '../../components/Icon';
import MemberElement from './components/MemberElement';
import { showToast } from '../../utility/handleToast';
import { useUser } from '../../store/useUser';
import { shareCode } from '../../utility/share';

export default function TeamSpaceManage() {
  const { teamId } = useParams();
  const locationState = useLocation().state;
  const [iamLeader, setIamLeader] = useState(false);
  const { data: team } = useTeamInfo(teamId);
  const { userId } = useUser();
  const navigate = useNavigate();
  const { mutate: inviteTeam } = useInviteTeam();
  const isEnded = locationState.isEnded;

  useEffect(() => {
    if (team) {
      if (team?.teamResponse?.leader?.id == userId) {
        setIamLeader(true);
      } else {
        setIamLeader(false);
      }
    }
  }, [team]);

  function convertDateFormat(dateStr = '1900-01-01') {
    const [year, month, day] = dateStr.split('-');
    return `${year.slice(2)}.${month}.${day}`;
  }

  const handleEditTeam = () => {
    navigate(`/teamspace/manage/${teamId}/edit`, {
      state: team,
    });
  };

  const handleInviteTeam = () => {
    inviteTeam(teamId, {
      onSuccess: (data) => {
        const inviteCode = data.token;
        shareCode(inviteCode);
      },
    });
  };

  return (
    <div className='flex flex-col'>
      <StickyWrapper>
        <NavBar
          canPop={true}
          title='팀 스페이스 관리'
          onClickPop={() => {
            navigate(-1);
          }}
        />
      </StickyWrapper>
      <div className='mt-6 mb-8 flex justify-between'>
        <div className='header-1 flex items-center gap-2 text-gray-100'>
          <h1 className='text-gray-100'>{team?.teamResponse?.name}</h1>
          {iamLeader && !isEnded && (
            <button onClick={handleEditTeam}>
              <Icon name='edit' />
            </button>
          )}
        </div>
        {isEnded ?
          <p className='caption-4 w-fit self-center text-right break-words text-gray-100'>
            {convertDateFormat(team?.teamResponse?.startDate)} ~{' '}
            {convertDateFormat(team?.teamResponse?.endDate)}
          </p>
        : <button onClick={handleInviteTeam}>
            <Tag type={TagType.TEAM_NAME}>초대링크 복사</Tag>
          </button>
        }
      </div>
      <ul className='flex flex-col gap-4'>
        {team?.members.map((member) => (
          <MemberElement
            key={member.id}
            teamId={team.teamResponse.id}
            member={member}
            leaderId={team?.teamResponse?.leader?.id}
            iamLeader={iamLeader}
          />
        ))}
      </ul>
    </div>
  );
}
