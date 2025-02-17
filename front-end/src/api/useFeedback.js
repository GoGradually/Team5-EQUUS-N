import { api } from './baseApi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { showToast } from '../utility/handleToast';

export const useFeedbackReceived = (userId, params) => {
  return useQuery({
    queryKey: ['feedback-received', userId, params],
    retry: 0,
    queryFn: () => {
      let sendingParams = {
        filterHelpful: params.onlyLiked,
        sortOrder: params.sortBy === 'createdAt:desc' ? 'DESC' : 'ASC',
        page: params.page,
      };
      if (params.teamId) {
        sendingParams.teamId = params.teamId;
      }
      return api.get({
        url: `/api/feedbacks/receiver/${userId}`,
        params: sendingParams,
      });
    },
  });
};

export const useFeedbackSent = (userId, params) => {
  return useQuery({
    queryKey: ['feedback-sent', userId, params],
    retry: 0,
    queryFn: () => {
      let sendingParams = {
        filterHelpful: params.onlyLiked,
        sortOrder: params.sortBy === 'createdAt:desc' ? 'DESC' : 'ASC',
        page: params.page,
      };
      if (params.teamId) {
        sendingParams.teamId = params.teamId;
      }
      return api.get({
        url: `/api/feedbacks/sender/${userId}`,
        params: sendingParams,
      });
    },
  });
};

export const useFeedbackLike = (userId, feedbackId) => {
  return useMutation({
    mutationFn: () =>
      api.post({ url: `/api/member/${userId}/feedbacks/${feedbackId}/liked` }),
    onSuccess: () => {
      showToast('좋아요를 눌렀어요');
    },
    onError: (error) => {
      showToast(error.response.data.message);
    },
  });
};

export const useFeedbackLikeCancel = (userId, feedbackId) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: () =>
      api.delete({
        url: `/api/member/${userId}/feedbacks/${feedbackId}/liked`,
      }),
    onSuccess: () => {
      showToast('좋아요를 취소했어요');
    },
    onError: (error) => {
      showToast(error.response.data.message);
    },
  });
};

export const useFeedbackFavorite = () => {
  return useQuery({
    queryKey: ['feedback-preference'],
    queryFn: () => api.get({ url: '/api/feedback/preference' }),
    gcTime: 1000 * 60 * 60, // 바뀔 일이 거의 없는 데이터라서 1시간으로 설정
  });
};

export const useEditFavorite = () => {
  return useMutation({
    mutationFn: (data) =>
      api.post({ url: '/api/member/feedback-prefer', body: data }),
  });
};

export const useFeedbackFavoriteByUser = (data) => {
  return useQuery({
    queryKey: ['feedback-favorite-by-user'],
    queryFn: () =>
      api.get({ url: `/api/member/feedback-prefer?findMemberId=${data}` }),
    gcTime: 0, // 지난번 거랑 바뀌는게 보기 좋지 않아서 그냥 캐싱 X
  });
};

export const useFeedbackObjective = () => {
  return useQuery({
    queryKey: ['feedback-objective'],
    queryFn: () => api.get({ url: '/api/feedback/objective' }),
  });
};

export const useFeedbackRefinement = () => {
  return useMutation({
    mutationFn: (data) =>
      api.post({ url: '/api/feedback-refinement', body: data }),
  });
};
