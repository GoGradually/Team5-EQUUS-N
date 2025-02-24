import { api } from './baseApi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { showToast } from '../utility/handleToast';

export const useFeedbackReceived = (userId, params) => {
  return useQuery({
    queryKey: ['feedback-received', userId, params],
    gcTime: 0,
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
    gcTime: 0,
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

export const useFeedbackRequest = () => {
  const queryClient = useQueryClient();
  return useMutation({
    /**
     * 피드백 요청 전송
     * @param {object} props
     * @param {number} props.receiverId - 피드백을 받을 사람의 ID
     * @param {number} props.teamId - 팀 ID
     * @param {string} props.requestedContent - 요청 내용
     * @returns
     */
    mutationFn: ({ receiverId, teamId, requestedContent }) =>
      api.post({
        url: `/api/feedbacks/frequent/request`,
        body: {
          receiverId,
          teamId,
          requestedContent,
        },
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['feedback-sent'] });
    },
    onError: (error) => {
      showToast(error.message);
    },
  });
};

export const useFeedbackSelf = () => {
  // const queryClient = useQueryClient();
  return useMutation({
    /**
     * 피드백 요청 전송
     * @param {object} props
     * @param {number} props.writerId - 작성자 본인의 ID
     * @param {number} props.teamId - 팀 ID
     * @param {string} props.title - 제목
     * @param {string} props.content - 내용
     * @returns
     */
    mutationFn: ({ writerId, teamId, title, content }) =>
      api.post({
        url: `/api/retrospect`,
        body: {
          writerId,
          teamId,
          title,
          content,
        },
      }),
    onSuccess: (data) => {
      // queryClient.invalidateQueries({ queryKey: ['feedback-sent'] }); // 추후 고민...
    },
    onError: (error) => {
      console.error('전송실패', error);
    },
  });
};

export const useRegularFeedbackSend = () => {
  return useMutation({
    mutationFn: (data) =>
      api.post({
        url: '/api/feedbacks/regular',
        body: data,
      }),

    onError: (error) => {
      showToast(error.message);
    },
  });
};

export const useFrequnetFeedbackSend = () => {
  return useMutation({
    mutationFn: (data) =>
      api.post({
        url: '/api/feedbacks/frequent',
        body: data,
      }),
    onError: (error) => {
      showToast(error.message);
    },
  });
};

/**
 * 정기피드백 인원 조회 및 건너뛰기 훅
 * @param {string} scheduleId
 */
export const useRegularFeedback = (scheduleId) => {
  const { data } = useQuery({
    queryKey: ['check-regular', scheduleId],
    queryFn: () =>
      api.get({
        url: `/api/feedbacks/regular/request?scheduleId=${scheduleId}`,
      }),
    onError: (error) => {
      showToast(error.message);
    },
  });

  const mutation = useMutation({
    mutationFn: () =>
      api.delete({
        url: `/api/feedbacks/regular/request?scheduleId=${scheduleId}`,
      }),
  });

  return { data, mutation };
};

export const useWhoNeedFreqFeedback = (teamId) => {
  return useQuery({
    queryKey: ['check-frequent', teamId],
    queryFn: () =>
      api.get({
        url: `/api/feedbacks/frequent/request`,
        params: { teamId },
      }),
    gcTime: 0,
  });
};
