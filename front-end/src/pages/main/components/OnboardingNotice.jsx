import { useState } from 'react';
import guide1 from '../../../assets/images/guide1.png';
import guide2 from '../../../assets/images/guide2.png';
import { hideModal } from '../../../utility/handleModal';

// showModal(<OnboardingNotice />);

export default function OnboardingNotice({ setPushNoti }) {
  const [page, setPage] = useState(1);

  const handleClickNextButton =
    page === 1 ?
      () => setPage(2)
    : async () => {
        await setPushNoti();
        hideModal();
      };

  return (
    <div className='mobile:px-10 tablet:px-30 desktop:px-[30%] fixed inset-0 flex items-center justify-center px-5'>
      <div className='rounded-400 relative h-[466px] w-full bg-white p-7'>
        <div className='flex h-full flex-col items-center justify-between text-center'>
          <div>
            <h1 className='header-3 pt-2 text-gray-800'>
              피드한줌 더 편리하게 이용하기
            </h1>
            <div className='pt-3'>
              {page === 1 ?
                <p className='text-gray-600'>
                  사이트 접속 후&nbsp;
                  <span className='font-semibold'>'홈 화면에 추가'</span>를
                  눌러주세요.
                </p>
              : <p className='text-gray-600'>
                  팝업이 뜨면&nbsp;
                  <span className='font-semibold'>'알림 허용하기'</span>를
                  눌러주세요.
                </p>
              }
            </div>
          </div>
          <img
            src={page === 1 ? guide1 : guide2}
            className='absolute top-[55%] max-h-[250px] -translate-y-1/2 px-7'
          />
          <div className='flex w-full justify-between'>
            {page === 1 ?
              <div> </div>
            : <button
                className='rounded-300 body-3 w-[98px] bg-gray-200 py-2.5 text-gray-400'
                onClick={() => setPage(1)}
              >
                이전
              </button>
            }
            <button
              className='body-3 rounded-300 w-[98px] bg-lime-500 py-2.5'
              onClick={handleClickNextButton}
            >
              {page === 1 ? '다음' : '시작하기'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
