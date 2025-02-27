import { createContext, useContext, useReducer } from 'react';

// 초기 상태 (로컬 스토리지에서 불러오기)
const loadStateFromStorage = () => {
  const stortedUserId = localStorage.getItem('userId');

  return {
    userId: stortedUserId,
  };
};

// 리듀서 함수
const userReducer = (state, action) => {
  switch (action.type) {
    case 'SET_USER_ID':
      localStorage.setItem('userId', JSON.stringify(action.payload));
      return { ...state, userId: action.payload };
    case 'REMOVE_USER_ID':
      localStorage.removeItem('userId');
      return { ...state, userId: null };

    default:
      return state;
  }
};

// Context 생성
const UserContext = createContext(null);

// Provider 컴포넌트
export const UserProvider = ({ children }) => {
  const [state, dispatch] = useReducer(userReducer, loadStateFromStorage());

  return (
    <UserContext.Provider value={{ state, dispatch }}>
      {children}
    </UserContext.Provider>
  );
};

// 커스텀 훅
export const useUserContext = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUserContext must be used within a UserProvider');
  }
  return context;
};
