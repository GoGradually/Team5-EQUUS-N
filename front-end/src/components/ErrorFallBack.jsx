import MediumButton from './buttons/MediumButton';
import logo from '../assets/images/logo.png';

export const ErrorFallback = () => {
  return (
    <div className='flex h-dvh w-screen flex-col items-center justify-center gap-4 bg-gray-900'>
      <div className='mx-10 flex flex-col items-center gap-8'>
        <img
          src={logo}
          className='rounded-400 size-20 shadow-[0_8px_20px_-4px_rgba(0,0,0,0.25)] shadow-black'
        />
        <h1 className='text-4xl font-semibold text-gray-100'>문제 발생</h1>
        <p className='text-center whitespace-pre-line text-gray-300'>
          {
            '예상치 못한 문제가 발생했습니다.\n버튼을 눌러 메인화면으로 돌아갈 수 있습니다.'
          }
        </p>
        <MediumButton
          isOutlined={false}
          text='메인으로'
          onClick={() => (window.location.href = '/main')}
        />
      </div>
    </div>
  );
};
