import { useMutation, useQuery } from '@tanstack/react-query';
import { api } from './baseApi';

export const useSubscribe = () => {
  return useMutation({
    mutationFn: (subscription) =>
      api.post({
        url: '/api/push-notification/subscribe',
        body: subscription,
      }),
  });
};

export const useAppServerKey = () => {
  return useQuery({
    queryKey: ['app-server-key'],
    queryFn: () =>
      api.get({ url: '/api/push-notification/application-server-key' }),
  });
};
