import * as React from 'react';

import router from '../routes';
import useAuthStore from '../store/useAuthStore';
import isTokenExpired from '../utils/isTokenExpired';

export default function useClientSideTokenValidation() {
  const token = useAuthStore(state => state.token);
  const updateToken = useAuthStore(state => state.updateToken);

  // Validate the token persisted on the client-side
  React.useEffect(() => {
    if (!token) {
      // If there's no persisted token, then the user is a guest, so
      // there's no need to redirect the guest user, b/c they would be
      // on a public route, a 404 page or even a 403 page
      return;
    }

    if (isTokenExpired(token)) {
      updateToken('');
      console.error(
        'You have been logged out because your bearer token has expired',
      );
      router.navigate('/');
      return;
    }
  }, [token, updateToken]);
}
