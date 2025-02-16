/**
 * @typedef {object} ReportResult
 * @property {string} category - 카테고리
 * @property {number} goodCount - 긍정 개수
 * @property {number} badCount - 부정 개수
 * @property {number} goodPercent - 긍정 퍼센트
 * @property {number} badPercent - 부정 퍼센트
 */

/**
 * 리포트 결과 컴포넌트
 * @param {object} props
 * @param {ReportResult} props.result - 리포트 결과
 */
function Result({ result }) {
  return (
    <div className='flex w-full flex-col items-center'>
      <h2 className='subtitle-1 mb-3 text-gray-100'>{result.category}</h2>
      <div className='flex w-full justify-between gap-1'>
        <div className='rounded-l-100 h-4 flex-1 bg-gray-600' dir='rtl'>
          <div
            className={`rounded-l-100 h-full bg-lime-600/50`}
            style={{ width: `${result.badPercent}%` }}
          />
        </div>
        <div className='rounded-r-100 h-4 flex-1 bg-gray-600'>
          <div
            className={`rounded-r-100 h-full bg-lime-600`}
            style={{ width: `${result.goodPercent}%` }}
          />
        </div>
      </div>
      <div className='mt-2 flex w-full justify-between'>
        <p className='body-2 text-red-300'>🤔{result.badCount}</p>
        <p className='body-2 text-blue-300'>😀+{result.goodCount}</p>
      </div>
    </div>
  );
}

/**
 * 리포트 결과 컴포넌트
 * @param {object} props
 * @param {ReportResult[]} props.results - 리포트 결과 리스트
 */
export default function ReportResults({ results }) {
  const maxCount = results.reduce(
    (acc, result) => Math.max(acc, result.goodCount, Math.abs(result.badCount)),
    0,
  );

  const resultsWithPercentage = results.map((result) => {
    const goodPercent = Math.round(Math.abs(result.goodCount / maxCount) * 100);
    const badPercent = Math.round(Math.abs(result.badCount / maxCount) * 100);
    return { ...result, goodPercent, badPercent };
  });

  return (
    <div className='flex flex-col gap-3'>
      <h2 className='header-4 ml-1 text-gray-100'>한눈에 보기</h2>
      <div className='rounded-400 flex flex-col gap-6 bg-gray-800 p-5'>
        {resultsWithPercentage.map((result, i) => (
          <Result key={i} result={result} />
        ))}
      </div>
    </div>
  );
}
