import { Outlet, useLocation } from 'react-router-dom';
import { hideModal } from '../utility/handleModal';

const noPaddingPages = ['/main', '/calendar']; // 패딩을 제거할 페이지 리스트

function Layout() {
  const location = useLocation();
  const isNoPadding = noPaddingPages.includes(location.pathname);

  // 라우팅 시에 모달이 열려있을 경우를 대비, 모달을 닫음
  hideModal();

  return (
    <div
      className={`scrollbar-hidden mx-auto h-full w-full max-w-[430px] overflow-x-hidden overflow-y-auto ${!isNoPadding && 'px-5'}`}
    >
      <Outlet />
    </div>
  );
}

export default Layout;
