import { useUserContext } from './UserContext';

// 팀 관련 로직을 처리하는 커스텀 훅
export const useUser = () => {
  const { state, dispatch } = useUserContext();

  // 팀 리스트 설정
  const setUserId = (userId) => {
    dispatch({ type: 'SET_USER_ID', payload: userId });
  };

  const removeUserId = () => {
    dispatch({ type: 'REMOVE_USER_ID' });
  };

  return {
    userId: parseInt(state.userId),
    setUserId,
    removeUserId,
  };
};
