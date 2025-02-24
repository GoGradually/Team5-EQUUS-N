import MediumButton from './buttons/MediumButton';

export const ErrorFallback = () => {
  return (
    <div className='flex h-dvh w-screen flex-col items-center justify-center gap-4 bg-gray-900'>
      <div className='mx-10 flex flex-col items-center gap-8'>
        <img src={logo} className='size-20' />
        <h1 className='text-4xl font-semibold text-gray-100'>에러 발생</h1>
        <p className='text-center whitespace-pre-line text-gray-300'>
          {
            '예상치 못한 에러가 발생했습니다.\n버튼을 눌러 메인화면으로 돌아갈 수 있습니다.'
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
