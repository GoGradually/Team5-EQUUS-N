import Icon from '../../../components/Icon';

/**
 * 요청 받은 피드백 컨텐츠 컴포넌트
 */
export default function RequestedContent({ content }) {
  return (
    <>
      <summary className='flex size-fit cursor-pointer list-none items-center gap-2'>
        <Icon name='info' />
        <p className='rounded-300 button-2 size-fit bg-gray-800 px-2 py-1.5 text-gray-300'>
          상대방이 요청한 피드백이에요
        </p>
      </summary>
      <div className='text-gray-0 rounded-300 scrollbar-hidden mt-3 h-48 w-full overflow-auto p-5 break-words ring ring-gray-500'>
        {content}
      </div>
    </>
  );
}
