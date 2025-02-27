/**
 * sticky wrapper component
 * @param {object} props
 * @param {string} props.bgColor
 * @param {string} props.className
 * @param {ReactElement} props.children
 * @returns {ReactElement}
 */
export default function StickyWrapper({
  bgColor = 'gray-900',
  className,
  children,
}) {
  return (
    <div className={`sticky top-0 z-[999] w-full ${className}`}>
      {children}
      <div
        className={`absolute top-0 -z-10 h-full bg-${bgColor} left-[50%] w-screen -translate-x-[50%]`}
      />
    </div>
  );
}
