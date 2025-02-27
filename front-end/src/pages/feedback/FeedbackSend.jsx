import { useLocation, useNavigate } from 'react-router-dom';
import { ProfileImageWithText } from '../../components/ProfileImage';
import FooterWrapper from '../../components/wrappers/FooterWrapper';
import { useRegularFeedback } from '../../api/useFeedback';
import { hideModal, showModal } from '../../utility/handleModal';
import Modal, { ModalType } from '../../components/modals/Modal';
import MediumButton from '../../components/buttons/MediumButton';

export default function FeedbackSend() {
  const navigate = useNavigate();
  const locationState = useLocation().state;

  const { data: requesters, mutation } = useRegularFeedback(
    locationState.scheduleId,
  );

  const modal = (
    <Modal
      type={ModalType.SKIP}
      title='이번 피드백을 정말 건너뛰시겠어요?'
      mainButton={
        <MediumButton
          text='건너뛰기'
          onClick={() =>
            mutation.mutate(
              {},
              {
                onSuccess: () => {
                  hideModal();
                  navigate('/main');
                },
              },
            )
          }
          isOutlined={true}
          disabled={true}
        />
      }
      subButton={
        <MediumButton
          text='취소'
          onClick={() => hideModal()}
          isOutlined={true}
          disabled={true}
        />
      }
    />
  );

  return (
    <div className='flex size-full flex-col gap-8'>
      {requesters && requesters.length > 0 ?
        <>
          <h1 className='header-2 text-gray-0 mt-3 whitespace-pre-line'>
            {'피드백을 보낼 팀원을\n선택해 주세요'}
          </h1>
          <div className='grid grid-cols-4 gap-4'>
            {requesters.map((requester, index) => {
              const member = requester.requester;
              return (
                <ProfileImageWithText
                  key={index}
                  text={member.name}
                  iconName={`@animals/${member.profileImage.image}`}
                  color={member.profileImage.backgroundColor}
                  onClick={() =>
                    navigate('1', {
                      state: {
                        receiver: { name: member.name, id: member.id },
                        needToRedirectSelectionPage: requesters.length > 1,
                        ...locationState,
                      },
                    })
                  }
                />
              );
            })}
          </div>
          <FooterWrapper>
            <div className='mb-4 flex w-full justify-center'>
              <button
                className='button-2 text-gray-300 underline underline-offset-4'
                onClick={() => showModal(modal)}
              >
                이번 피드백 건너뛰기
              </button>
            </div>
          </FooterWrapper>
        </>
      : <h1 className='header-2 mx-auto mt-3 text-gray-400'>
          {/* 임시 */}
          {'작성할 피드백이 없습니다'}
        </h1>
      }
    </div>
  );
}
