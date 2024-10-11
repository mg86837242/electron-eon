import { useQuery } from '@tanstack/react-query';

import { getAuthUser } from '../api';
import router from '../routes';
import useAuthStore from '../store/useAuthStore';
import isTokenExpired from '../utils/isTokenExpired';

export default function useAuthUser() {
  const token = useAuthStore(state => state.token);
  const updateToken = useAuthStore(state => state.updateToken);

  const hasPersistedToken = !!token && !isTokenExpired(token);

  const {
    data: authUser,
    isPending: isAuthUserPending,
    isError,
  } = useQuery({
    queryKey: ['getAuthUser', token],
    queryFn: () => getAuthUser(token),
    enabled: hasPersistedToken,
    staleTime: 1_000 * 60 * 15,
    gcTime: 1_000 * 60 * 60,
  });

  if (isError) {
    updateToken('');
    console.error(
      'You have been logged out because your bearer token caused an error',
    );
    router.navigate('/');
  }

  return { authUser, isAuthUserPending };
}
