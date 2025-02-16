import { useNavigate } from 'react-router-dom';
import NavBar2 from '../../components/NavBar2';
import { useGetFeedbackReport } from '../../api/useMyPage';
import ReportKeywords from './components/ReportKeywords';
import ReportResults from './components/ReportResults';
import ReportTopKeywords from './components/ReportTopKeywords';
import MediumButton from '../../components/buttons/MediumButton';

export default function Report() {
  const navigate = useNavigate();

  /** @type {ReportType} */
  const { data: report } = useGetFeedbackReport();

  return (
    <div className='flex size-full flex-col items-center'>
      <NavBar2
        canPop={true}
        onClickPop={() => navigate(-1)}
        title='피드백 리포트'
      />
      {report &&
        (report.feedbackCount < 200 ?
          <>
            <h1 className='header-2 text-gray-0 mt-3 break-keep'>
              {`피드백이 ${report.requiredFeedbackCount}개 이상 모이면 리포트를 확인할 수 있어요`}
            </h1>
            <div className='h-28' />
            <ProgressCircle
              feedbackCount={report.feedbackCount}
              requiredFeedbackCount={report.requiredFeedbackCount}
            />
            <div className='h-20' />
            <MediumButton
              text='피드백 요청하러 가기'
              isOutlined={false}
<<<<<<< HEAD
              onClick={() => navigate('/main')}
=======
              onClick={navigate('/main')}
>>>>>>> 21810ee2cdad3cbd83059f43f8593359956ae857
            />
          </>
        : <div className='flex flex-col gap-8 py-6'>
            <ReportTopKeywords topKeywords={report.topKeywords} />
            <ReportResults results={report.overviews} />
            <ReportKeywords keywords={report.allKeywords} />
          </div>)}
    </div>
  );
}

const ProgressCircle = ({ feedbackCount, requiredFeedbackCount }) => {
  const size = 240; // size-[240px]의 크기
  const innerSize = 200; // size-[200px]의 크기
  const radius = 110; // 원의 반지름
  const circumference = 2 * Math.PI * radius; // 원의 둘레

  const progress = feedbackCount / requiredFeedbackCount;

  return (
    <div className='relative size-fit'>
      <div className='flex size-[240px] items-center justify-center rounded-full bg-gray-700 shadow-[6px_6px_10px_-1px_rgba(0,0,0,0.25)]'>
        <div className='flex size-[200px] items-center justify-center rounded-full bg-gray-900 inset-shadow-[4px_4px_6px_-1px_rgba(0,0,0,0.2)]'>
          <p className='header-2 absolute text-gray-100'>
            {requiredFeedbackCount - feedbackCount}개 남음
          </p>
        </div>
      </div>
      <svg
        xmlns='http://www.w3.org/2000/svg'
        version='1.1'
        className='absolute inset-0 size-[240px]'
      >
        <defs>
          <linearGradient id='GradientColor'>
            <stop offset='0%' stopColor='#ecc06f' />
            <stop offset='100%' stopColor='#C2EC6F' />
          </linearGradient>
        </defs>
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          strokeLinecap='round'
          fill='none'
          strokeWidth={(size - innerSize) / 2}
          stroke='url(#GradientColor)'
          strokeDasharray={circumference}
          strokeDashoffset={circumference * (1 - progress)}
          transform={`rotate(-90 ${size / 2} ${size / 2})`}
        />
      </svg>
    </div>
  );
};
