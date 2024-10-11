import * as React from 'react';

import { loginUser } from '../api';
import useAuthStore from '../store/useAuthStore';

export default function useAuthentication() {
  const updateToken = useAuthStore(state => state.updateToken);

  // This handler is not global b/c not all pages need it (e.g., the checkout page)
  const handleLogin = React.useCallback(
    async (username, password) => {
      const data = await loginUser({ username, password });

      updateToken(data);
    },
    [updateToken],
  );

  // This handler is not global b/c not all pages need it (e.g., the checkout page)
  const handleLogout = React.useCallback(
    callback => {
      try {
        updateToken('');

        callback && callback();
      } catch (error) {
        console.error('Error logging out:', error);
      }
    },
    [updateToken],
  );

  return React.useMemo(
    () => ({
      handleLogin,
      handleLogout,
    }),
    [handleLogin, handleLogout],
  );
}
