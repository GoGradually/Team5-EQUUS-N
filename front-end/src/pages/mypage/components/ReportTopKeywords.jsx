/**
 * @typedef {object} keyword
 * @property {string} keyword
 * @property {string} feeling
 * @property {number} count
 */

/**
 * 가장 많이 받은 키워드 컴포넌트
 * @param {Object} props
 * @param {keyword[]} props.topKeywords - 상위 키워드 리스트
 */
export default function ReportTopKeywords({ topKeywords }) {
  return (
    <div className='flex flex-col gap-3'>
      <h2 className='header-4 ml-1 text-gray-100'>가장 많이 받은 키워드</h2>
      <div className='flex w-full gap-3'>
        {topKeywords.map((keyword, i) => (
          <Card key={i} keyword={keyword} />
        ))}
      </div>
    </div>
  );
}

/**
 * 키워드 카드
 * @param {keyword} keyword
 */
const Card = ({ keyword }) => {
  console.log(keyword);
  return (
    <div className='rounded-400 flex w-full flex-col items-center justify-between gap-6 bg-gray-800 p-5'>
      <div className='text-3xl'>
        {keyword.feeling === '아쉬워요' ? '🤔' : '😊'}
      </div>
      <p className='subtitle-2 text-center break-keep whitespace-pre-line text-gray-100'>{`❝\n${keyword.keyword}\n❞`}</p>
      <div
        className={`rounded-100 caption-2 size-fit ${keyword.feeling === '아쉬워요' ? 'bg-[#FFA3A3]/20 text-[#FFA3A3]' : 'bg-[#91CBFB]/20 text-[#91CBFB]'} px-2 py-1`}
      >
        {Math.abs(keyword.count)}개 받음
      </div>
    </div>
  );
};
