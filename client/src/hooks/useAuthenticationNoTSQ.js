import * as React from 'react';
import axios from 'axios';

import { getAuthUser, loginUser } from '../api';
import router from '../routes';
import useAuthStore from '../store/useAuthStore';
import isTokenExpired from '../utils/isTokenExpired';

export default function useAuthentication() {
  const token = useAuthStore(state => state.token);
  const updateToken = useAuthStore(state => state.updateToken);
  const updateAuthUser = useAuthStore(state => state.updateAuthUser);
  const updateIsAuthUserPending = useAuthStore(
    state => state.updateAuthUserPending,
  );

  // Validate the persisted token, then sync with db by using the persisted token
  React.useEffect(() => {
    if (!token) {
      updateIsAuthUserPending(false);
      return;
    }

    if (isTokenExpired(token)) {
      console.error(
        'You have been logged out because your bearer token has expired',
      );
      updateToken('');
      updateAuthUser(null);
      updateIsAuthUserPending(false);
      router.navigate('/');
      return;
    }

    const source = axios.CancelToken.source();
    (async () => {
      try {
        const data = await getAuthUser(token, source.token);

        updateAuthUser(data);
        updateIsAuthUserPending(false);
      } catch (error) {
        if (!axios.isCancel(error)) {
          console.error(
            'You have been logged out because your bearer token caused an error',
          );
          updateToken('');
          updateAuthUser(null);
          updateIsAuthUserPending(false);
          router.navigate('/');
        }
      }
    })();
    return () => {
      source.cancel('Operation canceled by the user.');
    };
  }, [token, updateToken, updateAuthUser, updateIsAuthUserPending]);

  // This handler is not global b/c not all pages need it (e.g., the checkout page)
  const handleLogin = React.useCallback(
    async (username, password) => {
      updateIsAuthUserPending(true);
      updateAuthUser(null);

      // Fetched token will be sent to the store and persisted, for further use in the Effect hook; Effect hook will auto fire after the `token` state - a dependency - is changed
      const data = await loginUser({ username, password });

      updateToken(data);
    },
    [updateToken, updateAuthUser, updateIsAuthUserPending],
  );

  // This handler is not global b/c not all pages need it (e.g., the checkout page)
  const handleLogout = React.useCallback(
    callback => {
      try {
        updateToken('');
        updateAuthUser(null);

        callback && callback();
      } catch (error) {
        console.error('Error logging out:', error);
      }
    },
    [updateToken, updateAuthUser],
  );

  return React.useMemo(
    () => ({
      handleLogin,
      handleLogout,
    }),
    [handleLogin, handleLogout],
  );
}
