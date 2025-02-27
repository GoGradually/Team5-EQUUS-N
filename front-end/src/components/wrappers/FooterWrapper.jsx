/**
 * FooterWrapper component
 * @param {object} props
 * @param {string} props.bgColor
 * @param {ReactElement} props.children
 * @returns {ReactElement}
 */
export default function FooterWrapper({ bgColor = 'gray-900', children }) {
  return (
    <footer className='fixed right-0 bottom-0 left-0 mx-5 flex justify-center pt-3 pb-8 *:max-w-[390px]'>
      {children}
      <div
        className={`absolute bottom-0 -z-10 h-full bg-${bgColor} left-[50%] w-screen -translate-x-[50%]`}
      />
    </footer>
  );
}
