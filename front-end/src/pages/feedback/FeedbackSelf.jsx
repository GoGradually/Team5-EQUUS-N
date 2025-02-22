import NavBar2 from '../../components/NavBar2';
import StickyWrapper from '../../components/wrappers/StickyWrapper';
import TextArea from '../../components/TextArea';
import LargeButton from '../../components/buttons/LargeButton';
import FooterWrapper from '../../components/wrappers/FooterWrapper';
import { useState } from 'react';
import { showToast } from '../../utility/handleToast';
import { useFeedbackSelf } from '../../api/useFeedback2';
import { useNavigate } from 'react-router-dom';
import CustomInput from '../../components/CustomInput';
import { useUser } from '../../useUser';
import { useTeam } from '../../useTeam';
import { checkLength } from '../../utility/inputChecker';

export default function FeedbackSelf() {
  const [titleContent, setTitleContent] = useState('');
  const [textLength, setTextLength] = useState(0);
  const [textContent, setTextContent] = useState('');

  const navigate = useNavigate();

  const { userId } = useUser();
  const { selectedTeam } = useTeam();
  const mutation = useFeedbackSelf();

  const titleMaxLength = 30;
  const contentMaxLength = 400;

  const validation = () => {
    if (titleContent.length === 0) {
      showToast('제목을 입력해주세요');
      return false;
    }
    if (titleContent.length > titleMaxLength) {
      showToast('제목을 30자 이하로 작성해주세요');
      return false;
    }
    if (textLength === 0) {
      showToast('내용을 입력해주세요');
      return false;
    }
    if (textLength > contentMaxLength) {
      showToast('내용을 400byte 이하로 작성해주세요');
      return false;
    }

    return true;
  };

  return (
    <div className='flex size-full flex-col'>
      <StickyWrapper>
        <NavBar2
          canPop={true}
          title='회고 작성하기'
          onClickPop={() => {
            navigate('/main');
          }}
        />
      </StickyWrapper>
      <div className='h-6' />
      <CustomInput
        content={titleContent}
        setContent={(value) => {
          const newValue = checkLength(value, titleMaxLength);
          setTitleContent(newValue);
        }}
        isForRetrospect={true}
        hint='제목을 입력해주세요'
      />
      <div className='h-6' />
      <TextArea
        textLength={textLength}
        setTextLength={setTextLength}
        setTextContent={setTextContent}
      />
      <FooterWrapper>
        <LargeButton
          isOutlined={false}
          realDisabled={!mutation.isIdle}
          text={'완료'}
          disabled={
            textLength === 0 || titleContent.length === 0 ? true : false
          }
          onClick={() => {
            if (validation())
              mutation.mutate(
                {
                  writerId: userId,
                  teamId: selectedTeam,
                  title: titleContent,
                  content: textContent,
                },
                {
                  onSuccess: () =>
                    navigate(`/feedback/complete/?type=${'RETROSPECT'}`, {
                      replace: true,
                    }),
                },
              );
          }}
        />
      </FooterWrapper>
    </div>
  );
}
