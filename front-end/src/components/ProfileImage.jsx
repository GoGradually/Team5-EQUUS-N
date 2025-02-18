import { bgColors, iconNames } from '../constants/IconRelated';
import Icon from './Icon';

/**
 * 프로필 이미지 컴포넌트
 * @param {object} props
 * @param {string} props.text - 프로필 이미지 텍스트
 * @param {string} props.iconName - 프로필 이미지 아이콘 이름
 * @param {string} props.color - 프로필 이미지 색상
 * @param {function} props.onClick - 프로필 이미지 클릭 함수
 * @returns {JSX.Element} - 프로필 이미지 컴포넌트
 */
export function ProfileImageWithText({ text, iconName, color, onClick }) {
  return (
    <div
      className='flex max-w-[64px] cursor-pointer flex-col items-center gap-2'
      onClick={onClick}
    >
      <ProfileImage iconName={iconName} color={color} isButton={true} />
      <p className='caption-1 text-white'>{text}</p>
    </div>
  );
}

/**
 * 프로필 이미지 컴포넌트
 * @param {object} props
 * @param {string} props.iconName - 프로필 이미지 아이콘 이름
 * @param {string} props.color - 프로필 이미지 색상
 * @param {boolean} props.isButton - 버튼 여부
 * @returns {JSX.Element} - 프로필 이미지 컴포넌트
 */
export default function ProfileImage({ iconName, color, isButton }) {
  // iconName이 없을 때 기본 아이콘 표시
  if (!iconName) {
    return (
      <div className='flex aspect-square w-full items-center justify-center rounded-full bg-gray-700 text-2xl'>
        🔗
      </div>
    );
  }

  // dots 아이콘 처리
  if (iconName === 'dots') {
    return (
      <div className='flex aspect-square h-8 w-8 items-center justify-center rounded-full bg-gray-200 p-1'>
        <Icon
          name='dots'
          className={'flex items-center justify-center'}
          color={'var(--color-gray-800)'}
        />
      </div>
    );
  }

  // 동물 아이콘 처리
  if (iconName.includes('@animals')) {
    return (
      <div className='relative w-full overflow-hidden rounded-full'>
        <div
          className={`h-full ${isButton ? 'p-2.5' : 'p-1.5'}`}
          style={{ backgroundColor: color }}
        >
          <Icon name={iconName} />
        </div>
        {isButton && (
          <div className='absolute inset-0 flex items-center justify-center bg-gray-700/80 opacity-0 backdrop-blur-xs transition-opacity hover:opacity-100'>
            <div className='h-6 w-6'>
              <Icon name='send' />
            </div>
          </div>
        )}
      </div>
    );
  }

  // 기타 아이콘 처리
  return (
    <div className='flex aspect-square items-center justify-center rounded-full bg-gray-700 p-2.5'>
      <Icon name={iconName} />
    </div>
  );
}

/**
 * 랜덤 프로필 이미지 생성 함수
 * @returns {object} - 랜덤 프로필 이미지 객체
 */
export function getRandomProfile() {
  const randomIconName =
    iconNames[Math.floor(Math.random() * iconNames.length)];
  const randomColor = bgColors[Math.floor(Math.random() * bgColors.length)];
  return { image: randomIconName, backgroundColor: randomColor };
}

// 미리 짜둔 아이콘 변경 관련 코드
// const [iconName, setIconName] = useState('Whale');
// const [color, setColor] = useState('red');

// const handleChangeProfileImage = () => {
//   const { randomIconName, randomColor } = getRandomProfile();
//   setIconName(randomIconName);
//   setColor(randomColor);
// };

// <ProfileImage iconName={iconName} color={color} />
