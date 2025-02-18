import { api } from './baseApi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

/**
 *  팀 목록을 가져오는 훅 (쿠키에 있는 sessionId로 조회)
 */
export const useMyTeams = ({ enabled }) => {
  return useQuery({
    queryKey: ['myTeams'],
    queryFn: () => api.get({ url: '/api/team/my-teams' }),
    enabled: enabled,
    gcTime: 0,
  });
};

/**
 * 메인카드 데이터를 가져오는 훅
 * @param {number} teamId
 */
export const useMainCard = (teamId) => {
  return useQuery({
    queryKey: ['mainCard', teamId],
    queryFn: () => api.get({ url: `/api/team/${teamId}/schedule` }), // 임시
    enabled: !!teamId,
  });
};

/**
 * 메인카드2 데이터를 가져오는 훅
 * @param {number} teamId
 */
export const useMainCard2 = (teamId) => {
  return useQuery({
    queryKey: ['mainCard2', teamId],
    queryFn: () => api.get({ url: `/api/team/${teamId}/members` }),
    enabled: !!teamId,
  });
};

/**
 * 사용자의 알람 데이터를 가져오고, 알람을 읽음으로 표시하는 훅
 */
export const useNotification = (teamId) => {
  const queryClient = useQueryClient();

  const { data, isLoading, isError } = useQuery({
    queryKey: ['notification', teamId],
    queryFn: () => api.get({ url: '/api/notification' }),
    gcTime: 0,
    enabled: !!teamId,
  });

  const markAsReadMutation = useMutation({
    mutationFn: (data) =>
      api.post({ url: '/api/notification/mark-as-read', body: data }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notification'] });
    },
  });

  return {
    data,
    isLoading,
    isError,
    markAsRead: markAsReadMutation.mutate,
  };
};
