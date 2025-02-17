import { api } from './baseApi';
import { useMutation, useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { showToast } from '../utility/handleToast';
import { useUser } from '../useUser';
import { useJoinTeam } from './useTeamspace';
import { useTeam } from '../useTeam';
import { getRandomProfile } from '../components/ProfileImage';

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
      showToast(`로그인 실패: ${error.message}`);
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

export const useGoogleLogin = () => {
  const { setUserId } = useUser();
  const navigate = useNavigate();
  return useMutation({
    mutationFn: (code) =>
      api.post({ url: '/api/auth/google/login', body: { code } }),
    onSuccess: (data) => {
      console.log(data);
      if (data.isAuthenticated) {
        setUserId(data.loginResponse.userId);
        navigate('/main', { replace: true });
      } else {
        const token = data.googleSignupToken.token;
        navigate('/feedback/favorite?process=signup', {
          replace: true,
          state: { profileImage: getRandomProfile(), token: token },
        });
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
