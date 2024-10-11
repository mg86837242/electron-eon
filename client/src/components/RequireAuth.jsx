import * as React from 'react';
import {
  Navigate,
  Outlet,
  useLocation,
  useOutletContext,
} from 'react-router-dom';
import PropTypes from 'prop-types';

import useAuthUser from '../hooks/useAuthUser';
import useAuthStore from '../store/useAuthStore';
import isTokenExpired from '../utils/isTokenExpired';

import Spinner from './Spinner';

export default function RequireAuth({ permittedRoles }) {
  const location = useLocation();
  const outletContext = useOutletContext();
  const token = useAuthStore(state => state.token);
  const { authUser, isAuthUserPending } = useAuthUser();

  // `token` state is persisted, thus can be immediately used to tell if the user needs to be redirected
  const hasPersistedToken = !!token && !isTokenExpired(token);
  const canAccess = permittedRoles?.includes(authUser?.claims?.scope);

  if (!hasPersistedToken) {
    return <Navigate to='/login' state={{ from: location }} replace />;
  }

  if (isAuthUserPending) {
    return <Spinner />;
  }

  if (canAccess) {
    return outletContext ? <Outlet context={outletContext} /> : <Outlet />;
  } else {
    return <Navigate to='/login' state={{ from: location }} replace />;
  }
}

RequireAuth.propTypes = {
  permittedRoles: PropTypes.arrayOf(PropTypes.string).isRequired,
};

// References:
// -- curr solution is to call custom hook to apply global Effect to validate token on the client-side and to use TSQ to
//    validate token on the server-side, by fetching authenticated user in a custom hook, so that I don't need to code
//    isAuthUserPending` and even error-related states myself; fetching token – tied to the login handler – is
//    straightforward and does not require TSQ
// -- alternatively, use a `loadingElapsed` state to delay the evaluation of `canAccess`, and render a spinner while
//    timed out (Effect hook + `setTimeout`); this can also work in conjunction with the curr solution
// -- another alternative is to cond providing router: https://stackoverflow.com/questions/62384395/protected-route-with-react-router-v6/64347082#64347082
//    => not recommended => "I would create a single route with all router, then in the loader of the routes I would
//    check the role of the user and if it doesn’t have access I would either render a 404 (to hide the existence of the
//    route) or redirect (if the user can know that exists but it doesn’t have access)", by Sergio

// References for passing ctx (e.g., role info) to the loader
// -- the official proposal of middleware feature to help passing the ctx to the loader:
//    https://github.com/remix-run/react-router/discussions/9564
// -- unstable middleware as of May 2024: https://reactrouter.com/en/main/routers/create-browser-router#middleware

// References for `isPending` and `status === 'pending'
// -- "`pending` means you have no data. If the query is not enabled, it also has no data, that's why it's `pending`":
//    https://github.com/TanStack/query/discussions/7329
