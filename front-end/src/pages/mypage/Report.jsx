import { useLocation, useNavigate } from 'react-router-dom';
import NavBar2 from '../../components/NavBar2';
import { useGetFeedbackReport } from '../../api/useMyPage';
import { useEffect, useState } from 'react';
import ReportKeywords from './components/ReportKeywords';
import ReportResults from './components/ReportResults';
import ReportTopKeywords from './components/ReportTopKeywords';

export default function Report() {
  const navigate = useNavigate();

  /** @type {ReportType} */
  const { data: report } = useGetFeedbackReport();

  return (
    <div className='flex size-full flex-col'>
      <NavBar2
        canPop={true}
        onClickPop={() => navigate(-1)}
        title='피드백 리포트'
      />
      {report &&
        // (report.feedbackCount < 20 ?
        (report.feedbackCount < report.requiredFeedbackCount ?
          <>
            <h1 className='header-2 text-gray-0 mt-3 break-keep'>
              {`피드백이 ${report.requiredFeedbackCount}개 이상 모이면 리포트를 확인할 수 있어요`}
            </h1>
            동그라미
          </>
        : <div className='flex flex-col gap-8 py-6'>
            <ReportTopKeywords topKeywords={report.topKeywords} />
            <ReportResults
              results={[
                {
                  title: '커뮤니케이션',
                  goodCount: 23,
                  badCount: 65,
                },
                {
                  title: '협업 태도',
                  goodCount: 35,
                  badCount: 5,
                },
                {
                  title: '결과물과 업무',
                  goodCount: 34,
                  badCount: 53,
                },
              ]}
            />
            <ReportKeywords
              reports={[
                { keyword: '직설적인 말투', count: 1123, isPositive: true },
                {
                  keyword: '완곡하게 완곡하게 완곡하게 완곡하게 완곡하게',
                  count: 123,
                  isPositive: false,
                },
                { keyword: '대안을 제시하는', count: 12, isPositive: true },
                { keyword: '비판적인', count: 1, isPositive: false },
              ]}
            />
          </div>)}
    </div>
  );
}
