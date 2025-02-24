import { useState } from 'react';
import NavBar from '../../components/NavBar';
import StickyWrapper from '../../components/wrappers/StickyWrapper';
import { useNavigate } from 'react-router-dom';
import TeamElement from './components/TeamElement';
import { checkIsFinished } from '../../utility/time';
import { useTeam } from '../../store/useTeam';

export default function TeamSpaceList() {
  const navigate = useNavigate();
  const [showEndedTeams, setShowEndedTeams] = useState(false);
  const { teams } = useTeam(true);

  return (
    <div className='scrollbar-hidden flex h-full flex-col overflow-x-hidden overflow-y-auto'>
      <StickyWrapper>
        <NavBar
          canPop={true}
          title='팀 스페이스 관리'
          onClickPop={() => {
            navigate(-1);
          }}
        />
      </StickyWrapper>
      <div className='button-2 my-6 flex justify-end gap-2 text-gray-400'>
        <button onClick={() => setShowEndedTeams(false)}>
          <p className={`${showEndedTeams ? '' : 'text-lime-500'}`}>진행중</p>
        </button>
        <p>•</p>
        <button onClick={() => setShowEndedTeams(true)}>
          <p className={`${showEndedTeams ? 'text-lime-500' : ''}`}>종료됨</p>
        </button>
      </div>
      {teams.length > 0 ?
        <ul className='flex flex-col gap-4'>
          {teams
            .filter((team) => {
              if (showEndedTeams) {
                return checkIsFinished(team.endDate);
              }
              return !checkIsFinished(team.endDate);
            })
            .map((team) => (
              <button
                onClick={() =>
                  navigate(`/teamspace/manage/${team.id}`, {
                    state: { isEnded: showEndedTeams },
                  })
                }
                key={team.id}
              >
                <TeamElement
                  teamId={team.id}
                  teamName={team.name}
                  startDate={team.startDate}
                  endDate={team.endDate}
                  teamMembers={team.teamMembers}
                  isEnded={showEndedTeams}
                />
              </button>
            ))}
        </ul>
      : <div className='flex h-full flex-col items-center justify-center gap-4 text-gray-300'>
          <p className='text-5xl'>😢</p>
          <p>소속된 팀이 없어요</p>
        </div>
      }
    </div>
  );
}
