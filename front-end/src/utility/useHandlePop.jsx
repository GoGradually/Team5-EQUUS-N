import { useEffect } from 'react';

const useHandlePop = (handlePopState) => {
  useEffect(() => {
    window.addEventListener('popstate', handlePopState);
    return () => {
      window.removeEventListener('popstate', handlePopState);
    };
  }, []);
};

export default useHandlePop;
