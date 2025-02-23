import NavBar from '../auth/components/NavBar';
import LargeButton from '../../components/buttons/LargeButton';
import { useLocation, useNavigate, useSearchParams } from 'react-router-dom';
import { useInviteTeam } from '../../api/useTeamspace';
import { shareCode } from '../../utility/share';

export default function TeamSpaceMakeSuccess() {
  const [searchParams] = useSearchParams();
  const location = useLocation();
  const teamName = searchParams.get('teamName');
  const navigate = useNavigate();
  const isFirstVisit = location.state?.from === '/first';

  const { mutate: inviteTeam } = useInviteTeam();

  const handleInviteTeam = () => {
    if (location.state.teamId) {
      inviteTeam(location.state.teamId, {
        onSuccess: async (data) => {
          const inviteCode = data.token;
          shareCode(inviteCode);
        },
      });
    }
  };

  return (
    <div className='relative flex h-dvh w-full flex-col justify-start'>
      <NavBar title={`${teamName} 팀 생성이 완료되었어요!`} />
      <div className='h-3' />
      <p className='body-1 text-gray-0'>
        아래 링크를 통해 팀원들을 초대해주세요
      </p>
      <div className='h-4' />
      <div className='relative'>
        <LargeButton
          text='초대링크 공유하기 🔗'
          isOutlined={true}
          onClick={handleInviteTeam}
        />
        <div className='absolute -bottom-12 left-1/2 flex w-[200px] -translate-x-1/2 animate-pulse items-center justify-center rounded-full bg-gray-700 px-6 py-1 text-gray-200 before:absolute before:-top-4 before:left-1/2 before:-translate-x-1/2 before:border-[8px] before:border-transparent before:border-b-gray-700'>
          <p className='caption-1'>나중에도 초대할 수 있어요!</p>
        </div>
      </div>
      {/* 다음 버튼 */}
      <div className='absolute right-0 bottom-[34px] left-0 flex flex-col bg-gray-900'>
        <LargeButton
          text={isFirstVisit ? '시작하기' : '홈으로'}
          isOutlined={false}
          onClick={() =>
            navigate('/main', {
              state: isFirstVisit ? { init: true } : null,
            })
          }
        />
      </div>
    </div>
  );
}
