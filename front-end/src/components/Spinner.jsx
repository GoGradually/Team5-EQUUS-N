export default function Spinner({ children, bgColor = 'bg-gray-800' }) {
  return (
    <div className='absolute inset-0 flex flex-col items-center justify-center'>
      <div className='relative mt-6 mb-10 flex items-center justify-center'>
        <div className='absolute size-10 animate-spin rounded-full bg-conic from-transparent from-5% to-lime-600' />
        <div className={`absolute size-[30px] rounded-full ${bgColor}`} />
      </div>
      {children}
    </div>
  );
}
