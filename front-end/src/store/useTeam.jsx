import { useEffect } from 'react';
import { useMyTeams } from '../api/useMainPage';
import { useTeamContext } from './TeamContext';

/**
 * 팀 관련 정보를 get, set 할 수 있는 훅
 * @param {boolean} needToRefreshTeam - 훅 수행시에, 팀 정보 api를 다시 호출할지 여부
 */
export const useTeam = (needToRefreshTeam = false) => {
  const { state, dispatch } = useTeamContext();

  // 팀 리스트 설정
  const setTeams = (teams) => {
    dispatch({ type: 'SET_TEAMS', payload: teams });
  };

  // 특정 팀 선택
  const selectTeam = (teamId) => {
    if (teamId) {
      dispatch({ type: 'SELECT_TEAM', payload: teamId });
    }
  };

  // 팀 리스트 제거
  const removeTeams = () => {
    dispatch({ type: 'REMOVE_TEAMS' });
  };

  // 특정 팀 제거
  const removeSelectedTeam = () => {
    dispatch({ type: 'REMOVE_SELECTED_TEAM' });
  };

  // useTeam 호출하면 팀 목록을 가져옴
  const { data: teamsData } = useMyTeams({ enabled: needToRefreshTeam });

  useEffect(() => {
    if (teamsData) {
      setTeams(teamsData);
    }
  }, [teamsData]);

  return {
    teams: state.teams,
    selectedTeam: state.selectedTeam,
    setTeams,
    selectTeam,
    removeTeams,
    removeSelectedTeam,
  };
};
