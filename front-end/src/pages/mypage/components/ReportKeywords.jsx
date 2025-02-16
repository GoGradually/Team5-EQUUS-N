import Tag, { TagType } from '../../../components/Tag';

/**
 * reports 타입
 * @typedef {object} ReportKeyword
 * @property {string} keyword - 키워드
 * @property {number} count - 개수
 * @property {boolean} feeling - 긍정적 or 부정적
 */

/**
 * 리포트 키워드 컴포넌트
 * @param {object} props
 * @param {ReportKeyword} props.keyword - 리포트 키워드
 * @returns {ReactElement} 리포트 키워드 컴포넌트
 */
function Keyword({ keyword }) {
  return (
    <div className='flex w-full items-center justify-between'>
      <Tag type={TagType.REPORT}>{keyword.keyword}</Tag>
      <p
        className={`subtitle-2 ml-4 ${keyword.feeling === '칭찬해요' ? 'text-blue-300' : 'text-red-300'}`}
      >
        {keyword.count}
      </p>
    </div>
  );
}

/**
 * 리포트 키워드 컴포넌트
 * @param {object} props
 * @param {object} props.keywords - 리포트 키워드 리스트
 * @returns {ReactElement} 리포트 키워드 컴포넌트
 */
export default function ReportKeywords({ keywords }) {
  // TODO: 데이터 형식 추후 확인 필요
  return (
    <div className='flex flex-col gap-3'>
      <h2 className='header-4 ml-1 text-gray-100'>전체 보기</h2>
      <div className='rounded-400 flex flex-col gap-3 bg-gray-800 p-5'>
        {keywords.map((report, i) => (
          <Keyword key={i} keyword={report} />
        ))}
      </div>
    </div>
  );
}
