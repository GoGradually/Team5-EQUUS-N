import ModalBase from '../modals/ModalBase';
import ToastBase from '../toasts/ToastBase';

export const BaseWrapper = ({ children }) => {
  return (
    <div className='h-dvh w-dvw bg-gray-900'>
      {children}
      <ModalBase />
      <ToastBase />
    </div>
  );
};
