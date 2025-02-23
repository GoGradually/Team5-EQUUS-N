import NavBar from './components/NavBar';
import CustomInput from '../../components/CustomInput';
import LargeButton from '../../components/buttons/LargeButton';
import logo from '../../assets/images/logo.png';
import Icon from '../../components/Icon';
import Certification from '../../components/Certification';
import { CertState } from '../../components/Certification';
import { useEffect, useState } from 'react';
import {
  isValidComplexity,
  isValidEmail,
  isEnoughLength,
  isWithin10Bytes,
  checkSignUpInfos,
  isValidPassword,
} from '../../utility/inputChecker';
import { useNavigate } from 'react-router-dom';
import { getRandomProfile } from '../../components/ProfileImage';

/**
 * 회원가입 페이지
 * @returns
 */
export default function SignUp() {
  const [email, setEmail] = useState('');
  const [certState, setCertState] = useState(CertState.BEFORE_SEND_CODE);
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const [isPasswordConfirmVisible, setIsPasswordConfirmVisible] =
    useState(false);
  const [nickName, setNickName] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    setNickName(nickName.trim());
  }, [nickName]);

  const handleClickNextButton = () => {
    if (
      checkSignUpInfos(certState, password, passwordConfirm, nickName) === true
    ) {
      navigate('/feedback/favorite?process=signup', {
        state: {
          email,
          password,
          name: nickName,
          profileImage: getRandomProfile(),
        },
      });
    }
  };

  return (
    <div className='relative flex h-screen w-full flex-col justify-start'>
      <NavBar
        title={
          <p className='flex items-center justify-between'>
            계정 만들기
            <img src={logo} className='w-8' />
          </p>
        }
      />
      <div className='h-2' />
      {/* 이메일 입력 */}
      <CustomInput
        label='이메일'
        hint='이메일을 입력해 주세요'
        content={email}
        setContent={certState !== CertState.AFTER_CHECK_CODE && setEmail}
        type='email'
      />
      <div className='h-4' />
      {/* 인증 컴포넌트 */}
      <Certification
        email={isValidEmail(email) ? email : ''}
        certState={certState}
        setCertState={setCertState}
      />
      <div className='h-6' />
      {/* 비밀번호 입력 */}
      <CustomInput
        label='비밀번호'
        hint='영문, 숫자 포함 8글자 이상'
        content={password}
        setContent={setPassword}
        isPassword={!isPasswordVisible}
        condition={[isValidComplexity, isEnoughLength]}
        notification={['영문, 숫자, 특수 문자 포함', '8글자 이상']}
        addOn={
          <button onClick={() => setIsPasswordVisible(!isPasswordVisible)}>
            <Icon
              name='eye'
              color={
                !isPasswordVisible ?
                  'var(--color-gray-500)'
                : 'var(--color-lime-500)'
              }
            />
          </button>
        }
      />
      {/* 비밀번호 확인 */}
      <CustomInput
        label='비밀번호 확인'
        hint='비밀번호를 재입력해주세요'
        content={passwordConfirm}
        setContent={setPasswordConfirm}
        isPassword={!isPasswordConfirmVisible}
        addOn={
          <button
            onClick={() =>
              setIsPasswordConfirmVisible(!isPasswordConfirmVisible)
            }
          >
            <Icon
              name={
                passwordConfirm.length > 0 && passwordConfirm === password ?
                  'checkBoxClick'
                : 'checkBoxNone'
              }
            />
          </button>
        }
      />
      <div className='h-6' />
      {/* 활동 이름 입력 */}
      <CustomInput
        label='활동 이름'
        hint='팀원이 보게 될 이름이에요'
        content={nickName}
        setContent={setNickName}
        condition={[(content) => isWithin10Bytes(content)]}
        notification={['한글 최대 5글자, 영어 최대 10글자']}
      />
      {/* 다음 버튼 */}
      <div className='absolute right-0 bottom-[34px] left-0 bg-gray-900'>
        <LargeButton
          text='다음'
          isOutlined={false}
          disabled={
            certState !== CertState.AFTER_CHECK_CODE ||
            !isValidPassword(password) ||
            passwordConfirm !== password ||
            nickName.length === 0
          }
          onClick={handleClickNextButton}
        />
      </div>
    </div>
  );
}
