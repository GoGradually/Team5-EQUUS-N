import { useLocation, useNavigate } from 'react-router-dom';
import NavBar from '../../components/NavBar';
import ProfileImage, { getRandomProfile } from '../../components/ProfileImage';
import AiButton from '../../components/buttons/AiButton';
import CustomInput from '../../components/CustomInput';
import { isWithin10Bytes } from '../../utility/inputChecker';
import LargeButton from '../../components/buttons/LargeButton';
import { useState } from 'react';
import { useEditMember } from '../../api/useMyPage';
import { showToast } from '../../utility/handleToast';

export default function ProfileEdit() {
  const navigate = useNavigate();
  const member = useLocation().state;

  const [editedMember, setEditedMemeber] = useState(member);
  const [isValidName, setIsValidName] = useState(true);

  const editMemberMutation = useEditMember();

  return (
    <div className='flex size-full flex-col'>
      <NavBar canClose={true} onClickClose={() => navigate(-1)} />
      <div className='mx-auto mb-9 flex flex-col items-center'>
        <div className='mb-4 size-20'>
          <ProfileImage
            iconName={`@animals/${editedMember.profileImage.image}`}
            color={editedMember.profileImage.backgroundColor}
          />
        </div>
        <AiButton
          onClick={() =>
            setEditedMemeber((prev) => ({
              ...prev,
              profileImage: getRandomProfile(),
            }))
          }
        >
          랜덤 변경
        </AiButton>
      </div>
      <CustomInput label='이메일' hint={member.email} type='email' />
      <div className='h-6' />
      <CustomInput
        label='활동 이름'
        hint='변경할 이름을 입력하세요'
        content={editedMember.name}
        setContent={(content) => {
          setEditedMemeber((prev) => ({ ...prev, name: content }));
          isWithin10Bytes(content) ?
            setIsValidName(true)
          : setIsValidName(false);
        }}
        condition={[(content) => isWithin10Bytes(content)]}
        notification={['한글 최대 5글자, 영어 최대 10글자']}
      />
      <div className='h-6' />
      <LargeButton
        text='수정 완료'
        onClick={() => {
          if (isDisabled(member, editedMember, isValidName))
            showToast('수정 사항을 확인해주세요');
          else
            editMemberMutation.mutate(editedMember, {
              onSuccess: () => {
                showToast('수정 완료');
                navigate(-1);
              },
            });
        }}
        disabled={isDisabled(member, editedMember, isValidName)}
        isOutlined={false}
      />
    </div>
  );
}

const isDisabled = (member, editedMember, isValidName) => {
  if (!isValidName) return true;
  if (
    member.name === editedMember.name &&
    member.profileImage === editedMember.profileImage
  )
    return true;
  if (editedMember.name.length === 0) return true;
  return false;
};
