import { api } from './baseApi';
import { useMutation, useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { showToast } from '../utility/handleToast';
import { useUser } from '../store/useUser';
import { useJoinTeam } from './useTeamspace';
import { useTeam } from '../store/useTeam';
import { getRandomProfile } from '../components/ProfileImage';
import { stopPush } from './usePushNoti';
import { showModal } from '../utility/handleModal';
import Modal from '../components/modals/Modal';

export const useSendVerifMail = () => {
  return useMutation({
    mutationFn: (data) =>
      api.post({
        url: '/api/auth/send-signup-verification-email',
        body: data,
      }),
  });
};

export const useVerifyToken = () => {
  return useMutation({
    mutationFn: (data) =>
      api.post({ url: '/api/auth/verify-signup-email-token', body: data }),
  });
};

export const useGetMember = (id) => {
  return useQuery({
    queryKey: ['member', id],
    gcTime: 0,
    queryFn: () => api.get({ url: '/api/member', params: { id } }),
  });
};

export const useEmailSignUp = () => {
  return useMutation({
    mutationFn: (data) =>
      api.post({ url: '/api/auth/email/signup', body: data }),
    onSuccess: () => {},
  });
};

export const useLogin = (teamCode) => {
  const navigate = useNavigate();
  const { setUserId } = useUser();
  const { mutate: joinTeam } = useJoinTeam();
  return useMutation({
    mutationFn: (data) =>
      api.post({ url: '/api/auth/email/login', body: data }),
    onSuccess: (data) => {
      const { email, message, userId } = data;
      setUserId(userId);
      if (teamCode) {
        joinTeam(teamCode);
      }
      navigate('/main');
    },
    onError: (error) => {
      if (error.status === 401) {
        showToast('이메일 또는 비밀번호가 올바르지 않습니다');
      }
    },
  });
};

export const useLogout = () => {
  const navigate = useNavigate();
  const { removeTeams } = useTeam();
  const { removeUserId } = useUser();
  return useMutation({
    mutationFn: () => api.post({ url: '/api/auth/logout' }),
    onSuccess: () => {
      removeTeams();
      removeUserId();
      stopPush();
      navigate('/', { replace: true });
    },
  });
};

export const useGetGoogleUrl = () => {
  return useQuery({
    queryKey: ['googleAuth'],
    queryFn: () => api.get({ url: '/api/auth/google/login-url' }),
  });
};

export const useGoogleLogin = (teamCode, errorModal) => {
  const { setUserId } = useUser();
  const { mutate: joinTeam } = useJoinTeam();
  const navigate = useNavigate();
  return useMutation({
    mutationFn: (code) =>
      api.post({ url: '/api/auth/google/login', body: { code } }),
    onSuccess: (data) => {
      if (data.isAuthenticated) {
        setUserId(data.loginResponse.userId);
        if (teamCode) {
          joinTeam(teamCode);
          localStorage.removeItem('tempTeamCode');
        }
        navigate('/main', { replace: true });
      } else {
        const token = data.googleSignupToken.token;
        navigate('/feedback/favorite?process=signup', {
          replace: true,
          state: { profileImage: getRandomProfile(), token: token },
        });
      }
    },
    onError: (error) => {
      if (error.status === 409) {
        showModal(errorModal);
      }
    },
  });
};

export const useGoogleSignup = () => {
  return useMutation({
    mutationFn: (data) =>
      api.post({ url: '/api/auth/google/signup', body: data }),
  });
};

export const useSendResetEmail = () => {
  return useMutation({
    mutationFn: (email) =>
      api.post({ url: '/api/auth/send-password-reset-email', body: email }),
  });
};

export const useVerifyResetEmail = () => {
  return useMutation({
    mutationFn: (body) =>
      api.post({
        url: '/api/auth/verify-password-reset-token',
        body: body,
      }),
  });
};

export const useResetPassword = () => {
  return useMutation({
    mutationFn: (body) =>
      api.post({
        url: '/api/auth/reset-password',
        body,
      }),
  });
};
