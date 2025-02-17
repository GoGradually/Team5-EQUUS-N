import { Outlet, useLocation } from 'react-router-dom';
import ModalBase from './components/modals/ModalBase';
import ToastBase from './components/toasts/ToastBase';

const noPaddingPages = ['/main', '/calendar']; // 패딩을 제거할 페이지 리스트

function Layout() {
  const location = useLocation();
  const isNoPadding = noPaddingPages.includes(location.pathname);

  return (
    <div className='h-dvh w-dvw bg-gray-900'>
      <div
        className={`scrollbar-hidden mx-auto h-full w-full max-w-[430px] overflow-x-hidden overflow-y-auto ${!isNoPadding && 'px-5'}`}
      >
        <Outlet />
      </div>
      <ModalBase />
      <ToastBase />
    </div>
  );
}

export default Layout;
