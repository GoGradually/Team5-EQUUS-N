import { useLocation, useNavigate } from 'react-router-dom';
import { ProfileImageWithText } from '../../components/ProfileImage';

export default function FeedbackSend() {
  const navigate = useNavigate();
  const location = useLocation();
  const members = location.state.members;

  return (
    <div className='flex size-full flex-col gap-8'>
      <h1 className='header-2 text-gray-0 mt-3 whitespace-pre-line'>
        {'피드백을 보낼 팀원을\n선택해 주세요.'}
      </h1>
      <div className='flex flex-wrap gap-4'>
        {members.map((member, index) => (
          <ProfileImageWithText
            key={index}
            text={member.name}
            iconName={`@animals/${member.iconName}`}
            color={member.color}
            onClick={() => navigate('1', { state: { member } })}
          />
        ))}
      </div>
    </div>
  );
}
