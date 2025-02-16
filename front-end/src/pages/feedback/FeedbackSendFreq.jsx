import { useLocation, useNavigate } from 'react-router-dom';
import { ProfileImageWithText } from '../../components/ProfileImage';
import FooterWrapper from '../../components/wrappers/FooterWrapper';
import { useWhoNeedFreqFeedback } from '../../api/useFeedback2';
import { hideModal, showModal } from '../../utility/handleModal';
import Modal, { ModalType } from '../../components/modals/Modal';
import MediumButton from '../../components/buttons/MediumButton';
import { useEffect, useState } from 'react';
import TextArea from '../../components/TextArea';
import LargeButton from '../../components/buttons/LargeButton';

export default function FeedbackSendFreq() {
  const navigate = useNavigate();
  const locationState = useLocation().state;
  const [selectedRequester, setSelectedRequester] = useState(null);

  const { data: whoNeedFreqFeedback } = useWhoNeedFreqFeedback(
    locationState.teamId,
  );

  return (
    <div className='flex size-full flex-col gap-8'>
      {whoNeedFreqFeedback && whoNeedFreqFeedback.length > 0 ?
        <>
          <div className='flex flex-col gap-[5px]'>
            <h1 className='header-2 text-gray-0 mt-3 whitespace-pre-line'>
              {selectedRequester ?
                `${selectedRequester.requester.name}님이 요청한 피드백이에요.`
              : '나에게 피드백을 요청한 팀원이에요.'}
            </h1>
            <h2 className='body-2 text-gray-200'>
              {selectedRequester ?
                '피드백을 보낼 때 참고해보세요!'
              : '피드백을 보낼 팀원을 선택하세요!'}
            </h2>
          </div>
          {!selectedRequester ?
            <div className='grid grid-cols-4 gap-4'>
              {whoNeedFreqFeedback.map((requester, index) => {
                console.log(requester);
                const member = requester.requester;
                return (
                  <ProfileImageWithText
                    key={index}
                    text={member.name}
                    iconName={`@animals/${member.profileImage.image}`}
                    color={member.profileImage.backgroundColor}
                    onClick={() => setSelectedRequester(requester)}
                  />
                );
              })}
            </div>
          : <div
              className={`rounded-300 relative flex h-fit w-full flex-col p-5 ring ring-gray-500`}
            >
              <textarea
                placeholder={selectedRequester.requestedContent}
                readOnly={true}
                className={`placeholder:body-1 scrollbar-hidden relative min-h-32 w-full resize-none outline-none placeholder:text-gray-400`}
              />
            </div>
          }
        </>
      : <h1 className='header-2 mx-auto mt-3 text-gray-400'>
          {/* 임시 */}
          {'작성할 피드백이 없습니다'}
        </h1>
      }
      {selectedRequester && (
        <FooterWrapper>
          <LargeButton
            isOutlined={false}
            text='다음'
            onClick={() =>
              navigate('/feedback/send/1', {
                state: {
                  receiver: {
                    name: selectedRequester.requester.name,
                    id: selectedRequester.requester.id,
                  },
                  requestedContent: selectedRequester.requestedContent,
                  ...locationState,
                },
              })
            }
          />
        </FooterWrapper>
      )}
    </div>
  );
}
