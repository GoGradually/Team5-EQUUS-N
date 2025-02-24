import { hideModal } from '../../utility/handleModal';
import Icon from '../Icon';

export const ModalType = Object.freeze({
  PROFILE: 'PROFILE',
  DOUBLE: 'DOUBLE',
  SINGLE: 'SINGLE',
  SINGLE_DELETE: 'SINGLE_DELETE',
  EXIT: 'EXIT',
  SKIP: 'SKIP',
  CUSTOM: 'CUSTOM',
});

/**
 * 모달 컴포넌트
 * @param {object} props
 * @param {keyof ModalType} props.type - 모달 타입
 * @param {ReactElement} props.profileImage - 프사
 * @param {string} props.title - 모달 내용
 * @param {string} props.content - 모달 내용
 * @param {ReactElement} props.mainButton - 메인 버튼
 * @param {ReactElement} props.subButton - 서브 버튼
 * @returns
 */
export default function Modal({
  type,
  profileImage,
  title,
  content,
  mainButton,
  subButton,
}) {
  return (
    <div className='rounded-400 text-gray-0 relative flex w-full flex-col bg-gray-800 p-4'>
      {type === ModalType.PROFILE && (
        <div className='flex flex-col items-center justify-center gap-3 pt-10'>
          {profileImage}
          <h2 className='subtitle-2 mb-7'>{title}</h2>
          {subButton}
          {mainButton}
          <button
            className='absolute top-4 right-4'
            onClick={() => hideModal()}
          >
            <Icon name='delete' />
          </button>
        </div>
      )}
      {type === ModalType.DOUBLE && (
        <>
          <h2 className='subtitle-2 mx-auto my-12'>{title}</h2>
          <div className='flex gap-3'>
            {subButton}
            {mainButton}
          </div>
        </>
      )}
      {type === ModalType.SINGLE && (
        <>
          <h2 className='subtitle-2 mx-auto my-12'>{title}</h2>
          {mainButton}
        </>
      )}
      {type === ModalType.SINGLE_DELETE && (
        <>
          <h2 className='subtitle-2 mx-auto my-12'>{title}</h2>
          {mainButton}
          <button
            className='absolute top-4 right-4'
            onClick={() => hideModal()}
          >
            <Icon name='delete' />
          </button>
        </>
      )}
      {type === ModalType.EXIT && (
        <>
          <h2 className='subtitle-2 mx-auto mt-6'>{title}</h2>
          <p className='caption-1 my-5 text-center break-keep whitespace-pre-line text-gray-200'>
            {subButton ?
              '계정 탈퇴 시 피드백 보관함에 기록된 모든 피드백이 삭제되며, 탈퇴하고자 하는 계정에 재접근이 불가해요. 그래도 탈퇴하시겠어요?'
            : '새로운 팀장에게 팀장 권한을 이전해 주세요. \n 팀장 변경 방법: 마이페이지 > 팀 스페이스 관리'
            }
          </p>
          <div className='flex gap-3'>
            {subButton}
            {mainButton}
          </div>
        </>
      )}
      {type === ModalType.SKIP && (
        <>
          <h2 className='subtitle-2 mx-auto mt-6'>{title}</h2>
          <p className='caption-1 my-5 text-center break-keep whitespace-pre-line text-gray-200'>
            {
              '피드백을 건너뛰면, 이번 일정에 관한 피드백을 더이상 작성할 수 없어요.'
            }
          </p>
          <div className='flex gap-3'>
            {subButton}
            {mainButton}
          </div>
        </>
      )}
      {type === ModalType.CUSTOM && (
        <>
          <h2 className='subtitle-2 mx-auto mt-6'>{title}</h2>
          <p className='caption-1 my-5 text-center break-keep whitespace-pre-line text-gray-200'>
            {content}
          </p>
          <div className='flex gap-3'>
            {subButton}
            {mainButton}
          </div>
        </>
      )}
    </div>
  );
}
