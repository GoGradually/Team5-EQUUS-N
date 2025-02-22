import Lottie from 'lottie-react';
import logoLottie from '../../assets/lotties/logo.json';
import { useEffect, useState } from 'react';
import logo from '../../assets/images/logo.png';
import googleLogo from '../../assets/images/google-logo.png';
import Icon from '../../components/Icon';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useGetGoogleUrl, useGetMember } from '../../api/useAuth';
import { useUser } from '../../useUser';
import { useJoinTeam } from '../../api/useTeamspace';

/**
 * 스플래시 페이지
 * @returns
 */
export default function Splash() {
  const { teamCode } = useParams();
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const { userId, setUserId } = useUser();
  const { data: member } = useGetMember(userId);
  const { mutate: joinTeam } = useJoinTeam();
  const { data: googleAuthUrl } = useGetGoogleUrl();

  /**
   * 버튼 클릭 핸들러
   * @param {string} path - 이동할 경로
   */
  const moveTo = (path) => {
    navigate(path, { state: teamCode });
  };

  useEffect(() => {
    setTimeout(() => {
      if (member) {
        setUserId(member.id);
        if (teamCode) {
          joinTeam(teamCode);
        }
        moveTo('/main');
      } else {
        setIsLoading(false);
      }
    }, 1500);
  }, [member]);

  const handleGoogleButton = () => {
    if (teamCode) {
      localStorage.setItem('tempTeamCode', teamCode);
    }
    window.location.href = googleAuthUrl.loginUrl;
  };

  return (
    <div className='flex h-full w-full items-center justify-center'>
      {isLoading ?
        // 로딩 중
        <div className='flex h-[20%] w-[20%] items-center justify-center'>
          <Lottie animationData={logoLottie} loop={true} autoplay={true} />
        </div>
      : <div className='rounded-400 flex h-[478px] w-full flex-col items-center justify-between bg-white px-7 py-8 transition-all duration-700 ease-in-out'>
          {/* 로고 */}
          <div className='flex flex-col items-center gap-5'>
            <img src={logo} alt='logo' className='h-[64px] w-[64px]' />
            <h1 className='header-1 text-gray-700'>피드한줌 시작하기</h1>
            <p className='body-1 text-center'>
              피드한줌은 팀플에서 서로의 성장을 돕는 <br />
              솔직하고 간편한 피드백 교환 서비스입니다.
            </p>
          </div>
          <div className='flex w-full flex-col items-center gap-5'>
            <div className='flex w-full flex-col items-center gap-2'>
              {/* 구글 로그인 */}
              <button
                className='flex w-full items-center gap-2 rounded-full bg-[#F5F4F7] px-5'
                onClick={handleGoogleButton}
              >
                <img src={googleLogo} alt='google' className='ml-4' />
                <p className='mx-auto py-[15px]'>Google로 시작하기</p>
              </button>
              {/* 이메일 로그인 */}
              <button
                className='flex w-full items-center gap-2 rounded-full border border-[#A3A3A3] bg-white px-5'
                onClick={() => moveTo('/signup')}
              >
                <div className='ml-4'>
                  <Icon name='mail' color='#595959' />
                </div>
                <p className='mx-auto py-[15px]'>이메일로 시작하기</p>
              </button>
            </div>
            {/* 로그인 링크 */}
            <div className='flex gap-1.5'>
              <p className='caption-1 text-[#414141]'>이미 회원이신가요?</p>
              <a
                className='caption-2 text-[#414141]'
                onClick={() => moveTo('/signin')}
              >
                로그인하기
              </a>
            </div>
          </div>
        </div>
      }
    </div>
  );
}
