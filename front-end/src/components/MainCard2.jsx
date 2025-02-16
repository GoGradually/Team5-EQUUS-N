import { useInviteTeam } from '../api/useTeamspace';
import { useTeam } from '../useTeam';
import { useUser } from '../useUser';
import { showToast } from '../utility/handleToast';
import MediumButton from './buttons/MediumButton';
import { ProfileImageWithText } from './ProfileImage';

/**
 * 메인 카드 2 컴포넌트
 * @param {object} props
 * @param {Member[]} props.teamMates - 팀원 목록
 * @param {string} props.teamMates[].name - 팀원 이름
 * @param {string} props.teamMates[].iconName - 팀원 아이콘 이름
 * @param {string} props.teamMates[].color - 팀원 아이콘 색상
 * @param {function} props.onClick - 아이콘 클릭 이벤트
 * @param {function} props.onReceivedFeedbackClick - 피드백 보관함 클릭 이벤트
 * @returns {JSX.Element} - 메인 카드 2 컴포넌트
 */
export default function MainCard2({
  teamMates,
  onClick,
  onReceivedFeedbackClick,
}) {
  const { mutate: inviteTeam } = useInviteTeam();
  const { selectedTeam } = useTeam();
  const { userId } = useUser();

  teamMates = [
    teamMates.find((mate) => mate.id === userId),
    ...teamMates.filter((mate) => mate.id !== userId),
  ];

  return (
    <div className={'rounded-400 mx-5 h-fit bg-gray-800 p-4'}>
      <p className='subtitle-2 pl-1 text-gray-100'>피드백 주고받기</p>
      <div className='my-5 grid grid-cols-4 gap-x-4 gap-y-4'>
        {teamMates.map((mate, index) => {
          return (
            <ProfileImageWithText
              key={index}
              text={mate.id === userId ? '나' : mate.name}
              iconName={`@animals/${mate.profileImage.image}`}
              color={mate.profileImage.backgroundColor}
              onClick={() => onClick(mate)}
            />
          );
        })}
        {teamMates.length < 4 && (
          <ProfileImageWithText
            text='팀원초대'
            onClick={() => {
              inviteTeam(selectedTeam, {
                onSuccess: (data) => {
                  const inviteCode = data.token;
                  navigator.clipboard.writeText(`feedhanjum.com/${inviteCode}`);
                  showToast('클립보드에 복사됨');
                },
              });
            }}
          />
        )}
      </div>
      <MediumButton
        text='피드백 보관함'
        onClick={onReceivedFeedbackClick}
        disabled={true}
      />
    </div>
  );
}
