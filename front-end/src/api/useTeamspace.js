import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { api } from './baseApi';
import { showToast } from '../utility/handleToast';
import { useTeam } from '../store/useTeam';

export const useMembers = (teamId) => {
  return useQuery({
    queryKey: ['members', teamId],
    queryFn: () => api.get({ url: `/api/team/${teamId}/members` }),
  });
};

export const useTeamInfo = (teamId) => {
  return useQuery({
    queryKey: ['team', teamId],
    queryFn: () => api.get({ url: `/api/team/${teamId}` }),
  });
};

export const useSetLeader = (teamId) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (leaderId) => {
      return api.post({ url: `/api/team/${teamId}/leader`, body: leaderId });
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['team', teamId]);
      showToast('팀장이 바뀌었어요');
    },
  });
};

export const useKickMember = (teamId) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (memberId) => {
      return api.delete({ url: `/api/team/${teamId}/member/${memberId}` });
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['team', teamId]);
      showToast('팀원을 내보냈어요');
    },
  });
};

export const useMakeTeam = () => {
  return useMutation({
    mutationFn: (teamInfo) => {
      const sendingData = teamInfo;
      return api.post({ url: `/api/team/create`, body: sendingData });
    },
    onSuccess: (data) => {
      return data;
    },
  });
};

export const useEditTeam = (teamId) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (teamInfo) => {
      const sendingData = teamInfo;
      return api.post({ url: `/api/team/${teamId}`, body: sendingData });
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['team', teamId]);
    },
  });
};

export const useJoinTeam = () => {
  const queryClient = useQueryClient();
  const { selectTeam } = useTeam();
  return useMutation({
    mutationFn: (code) => {
      const sendingData = { token: code };
      return api.post({ url: `/api/team/join`, params: sendingData });
    },
    onSuccess: (teamData) => {
      console.log(teamData);
      queryClient.invalidateQueries(['myTeams']);
      selectTeam(teamData.id);
      showToast('새 팀에 가입했어요');
    },
  });
};

export const useLeaveTeam = (teamId) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: () => {
      return api.delete({ url: `/api/team/${teamId}/leave` });
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['team', teamId]);
    },
  });
};

export const useInviteTeam = () => {
  return useMutation({
    mutationFn: (teamId) => {
      return api.post({ url: `/api/team/${teamId}/join-token` });
    },
    onSuccess: (data) => data,
  });
};
