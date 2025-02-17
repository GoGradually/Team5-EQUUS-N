import NavBar from './components/NavBar';
import CustomInput from '../../components/CustomInput';
import LargeButton from '../../components/buttons/LargeButton';
import logo from '../../assets/images/logo.png';
import Icon from '../../components/Icon';
import Certification from '../../components/Certification';
import { CertState } from '../../components/Certification';
import { useState } from 'react';
import {
  isValidComplexity,
  isValidEmail,
  isEnoughLength,
  checkResetPWInfos,
} from '../../utility/inputChecker';
import { useNavigate } from 'react-router-dom';
import { useResetPassword } from '../../api/useAuth';
import { showToast } from '../../utility/handleToast';

export default function PasswordReset() {
  const [email, setEmail] = useState('');
  const [certState, setCertState] = useState(CertState.BEFORE_SEND_CODE);
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const [isPasswordConfirmVisible, setIsPasswordConfirmVisible] =
    useState(false);
  const { mutate: resetPassword } = useResetPassword();
  const navigate = useNavigate();

  const handleConfirmButton = () => {
    if (checkResetPWInfos(certState, password, passwordConfirm) === true) {
      resetPassword(
        { email, newPassword: password },
        {
          onSuccess: () => {
            showToast('비밀번호를 성공적으로 변경했어요!');
            navigate('/');
          },
        },
      );
    }
  };

  return (
    <div className='relative flex h-screen w-full flex-col justify-start'>
      <NavBar
        title={
          <p className='flex items-center justify-between'>
            비밀번호 초기화
            <img src={logo} className='w-8' />
          </p>
        }
      />
      <div className='h-2' />
      {certState !== CertState.AFTER_CHECK_CODE ?
        <>
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
            type='resetPassword'
            email={isValidEmail(email) ? email : ''}
            certState={certState}
            setCertState={setCertState}
          />
        </>
      : <>
          {/* 비밀번호 입력 */}
          <CustomInput
            label='새 비밀번호'
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
            label='새 비밀번호 확인'
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
        </>
      }

      {/* 다음 버튼 */}
      <div className='absolute right-0 bottom-[34px] left-0 bg-gray-900'>
        <LargeButton
          text={certState !== CertState.AFTER_CHECK_CODE ? '다음' : '변경하기'}
          isOutlined={false}
          disabled={certState !== CertState.AFTER_CHECK_CODE}
          onClick={handleConfirmButton}
        />
      </div>
    </div>
  );
}
