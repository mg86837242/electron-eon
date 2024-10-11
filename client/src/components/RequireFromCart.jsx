import * as React from 'react';
import { Outlet, useLocation } from 'react-router-dom';

import useAuthStore from '../store/useAuthStore';

import Forbidden from './Forbidden';

export default function RequireFromCart() {
  const location = useLocation();
  const hasVisitedCheckout = useAuthStore(state => state.hasVisitedCheckout);

  const from = location.state?.from?.pathname || '/';

  // This also cover the case when the user has empty items in the cart, b/c this alludes to the fact that the user is not visiting the checkout page by clicking the checkout btn on the cart page since the button is disabled if having empty items in the cart
  return from === '/cart' || hasVisitedCheckout ? (
    <Outlet />
  ) : (
    <Forbidden message='Visit your cart first before checking out please' />
  );
}
