import { showToast } from '../utility/handleToast';
import { api } from './baseApi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

/**
 *
 * @param {Object} params
 * @param {String} params.teamId
 * @param {Date} params.startDay
 * @param {Date} params.endDay
 * @returns
 */
export const useGetSchedules = (params) => {
  return useQuery({
    queryKey: ['schedules', params.startDay],
    queryFn: () => {
      const sendingData = {
        startDay: params.startDay,
        endDay: params.endDay,
      };
      return api.get({ url: '/api/schedules', params: sendingData });
    },
  });
};

export const useGetTeamSchedules = (teamId) => {
  return useQuery({
    queryKey: ['schedules', teamId],
    queryFn: () => api.get({ url: `/api/team/${teamId}/schedules` }),
  });
};

export const useGetOneSchedule = (teamId, scheduleId) => {
  return useQuery({
    queryKey: ['schedules', teamId, scheduleId],
    queryFn: () =>
      api.get({ url: `/api/team/${teamId}/schedule/${scheduleId}` }),
  });
};

export const usePostSchedule = (teamId) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data) =>
      api.post({ url: `/api/team/${teamId}/schedule/create`, body: data }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['schedules'] });
      queryClient.invalidateQueries({ queryKey: ['mainCard', teamId] });
    },
    onError: (error) => {
      showToast(error.message);
    },
  });
};

export const useEditSchedule = (teamId) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ scheduleId, data }) => {
      return api.post({
        url: `/api/team/${teamId}/schedule/${scheduleId}`,
        body: data,
      });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['schedules'] });
    },
    onError: (error) => {
      showToast(error.message);
    },
  });
};

export const useDeleteSchedule = (teamId) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (scheduleId) =>
      api.delete({
        url: `/api/team/${teamId}/schedule/${scheduleId}`,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['schedules'] });
    },
  });
};
