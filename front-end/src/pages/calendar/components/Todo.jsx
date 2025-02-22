import CustomInput from '../../../components/CustomInput';
import Icon from '../../../components/Icon';
import { checkLength } from '../../../utility/inputChecker';

export default function Todo({ todos, setTodo, scrollRef }) {
  const todoMaxLength = 50;
  return (
    <div className='flex flex-col gap-2'>
      <h3 className='subtitle-2 text-gray-0'>내 역할</h3>
      <ul className='flex flex-col gap-2'>
        {todos.map((todo, index) => {
          return (
            <li key={index}>
              <CustomInput
                content={todo}
                setContent={(value) => {
                  const newValue = checkLength(value, todoMaxLength);
                  setTodo(
                    todos.map((todo, index2) => {
                      if (index2 === index) {
                        return newValue;
                      }
                      return todo;
                    }),
                  );
                }}
                isOutlined={false}
                bgColor='gray-700'
                addOn={
                  <button
                    onClick={() => {
                      setTodo(todos.filter((_, index2) => index2 !== index));
                    }}
                  >
                    <Icon name='deleteSmall' className='text-gray-300' />
                  </button>
                }
              />
            </li>
          );
        })}
      </ul>
      <button
        className='rounded-300 flex h-[52px] w-full items-center justify-center border border-gray-400'
        onClick={() => {
          setTodo([...todos, '']);
          scrollRef.current.scrollTo({
            top: scrollRef.current.scrollHeight,
            behavior: 'smooth',
          });
        }}
      >
        <Icon name='plusM' className='text-gray-400' />
      </button>
    </div>
  );
}
