import { api } from './baseApi';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

/**
 * 피드백 요청 커스텀 훅
 * @returns {object} - 피드백 요청 결과
 * @returns {function} - 피드백 요청 함수 로딩 여부
 *
 */
export const useGetSelfFeedback = (userId, params) => {
  return useQuery({
    queryKey: ['feedback-received', userId, params],
    retry: 0,
    queryFn: () => {
      let sendingParams = {
        sortOrder: params.sortBy === 'createdAt:desc' ? 'DESC' : 'ASC',
        page: params.page,
      };
      if (params.teamId) {
        sendingParams.teamId = params.teamId;
      }
      return api.get({
        url: `/api/retrospect/${userId}`,
        params: sendingParams,
      });
    },
  });
};

/**
 * 특정 유저 조회 커스텀 훅
 * @returns {Member} - 특정 유저 조회 결과
 */
export const useSearchMember = () => {
  return useQuery({
    queryKey: ['search-member'],
    queryFn: () => {
      return api.get({
        url: `/api/member`,
      });
    },
  });
};

/**
 * 회원 정보 변경 훅
 */
export const useEditMember = () => {
  return useMutation({
    mutationFn: (data) => api.post({ url: '/api/member', body: data }),
  });
};

/**
 * 피드백 리포트 조회
 */
export const useGetFeedbackReport = () => {
  return useQuery({
    queryKey: ['feedback-report'],
    queryFn: () => {
      return api.get({
        url: '/api/feedbacks/report',
      });
    },
  });
};
