import Accordion from './components/Accordion';
import AiButton from './components/buttons/AiButton';
import KeywordButton from './components/buttons/KeywordButton';
import ProgressBar from './components/progressBar';
import ReportKeywords from './components/ReportKeywords';
import Tag, { TagType } from './components/Tag';

function App2() {
  return (
    <div className='flex h-dvh w-dvw flex-col flex-wrap items-center justify-center gap-2 bg-gray-600'>
      <Tag type={TagType.TEAM_LEADER} />
      <Tag type={TagType.MY_ROLE} />
      <Tag type={TagType.MEMBER_ROLE}>백현식</Tag>
      <Tag type={TagType.TEAM_NAME}>프론트엔드팀</Tag>
      <Tag type={TagType.KEYWORD}>직설적인 말투</Tag>
      <Tag type={TagType.NUMBER}>1123</Tag>
      <Tag
        type={TagType.TEAM_SCHEDULE}
        children={{ date: '12일 목요일', time: '17:00' }}
      />

      <AiButton isActive={false} onClick={() => {}}>
        적용하기
      </AiButton>
      <AiButton isActive={true} onClick={() => {}}>
        재생성하기
      </AiButton>

      <KeywordButton isActive={true} onClick={() => {}}>
        완곡하게
      </KeywordButton>
      <KeywordButton isActive={false} onClick={() => {}}>
        대안을 제시하는
      </KeywordButton>

      <div className='flex h-[852px] w-[393px] flex-col bg-gray-900 px-5'>
        <Accordion
          selectedTeamId={1}
          teamList={[
            { id: 1, name: '프론트엔드팀' },
            { id: 2, name: '백엔드팀' },
            { id: 3, name: 'QA팀' },
          ]}
          onTeamClick={() => {}}
          isMainPage={true}
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
      </div>
      <div className='flex h-[852px] w-[393px] flex-col bg-gray-900 px-5'>
        <Accordion teamList={[]} isMainPage={true} />
      </div>
      <div className='flex h-[852px] w-[393px] flex-col bg-gray-900 px-5'>
        <ProgressBar currentStep={2} totalStep={3} />
        <Accordion
          selectedTeamId={2}
          teamList={[
            { id: 1, name: '프론트엔드팀' },
            { id: 2, name: '백엔드팀' },
            { id: 3, name: 'QA팀' },
          ]}
          isMainPage={false}
        />
      </div>
    </div>
  );
}

export default App2;
