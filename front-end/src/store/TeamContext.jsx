import { createContext, useContext, useReducer } from 'react';

// 초기 상태 (로컬 스토리지에서 불러오기)
const loadStateFromStorage = () => {
  const storedTeams = localStorage.getItem('teams');
  const storedSelectedTeam = localStorage.getItem('selectedTeam');

  return {
    teams: storedTeams ? JSON.parse(storedTeams) : [],
    selectedTeam: storedSelectedTeam ? JSON.parse(storedSelectedTeam) : null,
  };
};

// 리듀서 함수
const teamReducer = (state, action) => {
  switch (action.type) {
    case 'SET_TEAMS':
      localStorage.setItem('teams', JSON.stringify(action.payload));
      return { ...state, teams: action.payload };

    case 'SELECT_TEAM':
      localStorage.setItem('selectedTeam', JSON.stringify(action.payload));
      return { ...state, selectedTeam: action.payload };

    case 'REMOVE_TEAMS':
      localStorage.removeItem('teams');
      localStorage.removeItem('selectedTeam');
      return { teams: [], selectedTeam: null };

    case 'REMOVE_SELECTED_TEAM':
      localStorage.removeItem('selectedTeam');
      return { ...state, selectedTeam: null };

    default:
      return state;
  }
};

// Context 생성
const TeamContext = createContext(null);

// Provider 컴포넌트
export const TeamProvider = ({ children }) => {
  const [state, dispatch] = useReducer(teamReducer, loadStateFromStorage());

  return (
    <TeamContext.Provider value={{ state, dispatch }}>
      {children}
    </TeamContext.Provider>
  );
};

// 커스텀 훅
export const useTeamContext = () => {
  const context = useContext(TeamContext);
  if (!context) {
    throw new Error('useTeamContext must be used within a TeamProvider');
  }
  return context;
};
