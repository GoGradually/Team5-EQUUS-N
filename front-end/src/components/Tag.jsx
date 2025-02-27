export const TagType = Object.freeze({
  MY_ROLE: '내 역할',
  MEMBER_ROLE: 'MEMBER_ROLE',
  TEAM_LEADER: '팀장',
  TEAM_NAME: 'TEAM_NAME',
  KEYWORD: 'KEYWORD',
  NUMBER: 'NUMBER',
  TEAM_SCHEDULE: 'TEAM_SCHEDULE',
  REPORT: 'REPORT',
});

/**
 * 태그 컴포넌트
 * @param {object} props
 * @param {keyof typeof TagType} props.type - 태그 타입
 * @param {React.ReactNode} props.children - 태그 내용
 * @returns {React.ReactElement}
 * @example <Tag type={TagType.MY_ROLE} />
 * @example <Tag type={TagType.MEMBER_ROLE}>백현식</Tag>
 */
export default function Tag({ type, children }) {
  switch (type) {
    case TagType.MY_ROLE:
      return (
        <div className='bg-lime-0/20 rounded-100 caption-2 size-fit px-2 py-1 text-lime-500'>
          {TagType.MY_ROLE}
        </div>
      );
    case TagType.MEMBER_ROLE:
      return (
        <div className='rounded-100 caption-2 size-fit bg-gray-600 px-2 py-1 text-gray-300'>
          {children}
        </div>
      );
    case TagType.TEAM_LEADER:
      return (
        <div className='bg-lime-0/20 rounded-100 caption-2 size-fit border border-lime-100/40 px-1.5 py-0.5 text-lime-500'>
          {TagType.TEAM_LEADER}
        </div>
      );
    case TagType.TEAM_NAME:
      return (
        <div className='rounded-100 caption-2 size-fit border border-lime-500 px-2.5 py-1 text-lime-500'>
          {children}
        </div>
      );
    case TagType.KEYWORD:
      return (
        <div className='rounded-200 bg-lime-0/20 caption-2 size-fit px-2 py-1.5 text-lime-500'>
          {children}
        </div>
      );
    case TagType.NUMBER:
      return (
        <div className='rounded-100 subtitle-1 size-fit bg-lime-500 px-2.5 py-0.5 text-gray-800'>
          {children}
        </div>
      );
    case TagType.TEAM_SCHEDULE:
      return (
        <div className='body-3 flex size-fit items-center justify-between gap-1.5 rounded-full bg-lime-500 px-3.5 py-1 text-gray-800'>
          <p>{children.date}</p>
          <hr className='h-2.5 w-px bg-gray-800' />
          <p>{children.time}</p>
        </div>
      );
    case TagType.REPORT:
      return (
        <div className='rounded-200 body-1 size-fit bg-gray-700 px-2.5 py-2 text-gray-200'>
          {children}
        </div>
      );
    default:
      throw new Error('Invalid TagType!');
  }
}
