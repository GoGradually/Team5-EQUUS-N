import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const useHandlePop = (handlePopState) => {
  useEffect(() => {
    window.addEventListener('popstate', handlePopState);
    return () => {
      window.removeEventListener('popstate', handlePopState);
    };
  }, []);
};

export default useHandlePop;

export function blockPop(pathname, options = { replace: true }) {
  const navigate = useNavigate();
  navigate(pathname, options);
}
