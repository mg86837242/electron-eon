import { createBrowserRouter } from 'react-router-dom';

import {
  DefaultErrorBoundary,
  NotFound,
  PageLayout,
  RequireAuth,
  RequireFromCart,
} from '../components';
import {
  CartPage,
  CheckoutPage,
  LandingPage,
  OrderById,
  Orders,
  ProductsPage,
  Protected,
  SignInSide,
  SignUp,
} from '../pages';

const routes = [
  {
    path: '/',
    Component: LandingPage,
    ErrorBoundary: DefaultErrorBoundary,
  },
  {
    Component: PageLayout,
    ErrorBoundary: DefaultErrorBoundary,
    children: [
      {
        path: 'products',
        Component: ProductsPage,
        ErrorBoundary: DefaultErrorBoundary,
      },
      {
        Component: () => <RequireAuth permittedRoles={['ADMIN', 'CUSTOMER']} />,
        children: [
          {
            path: 'protected',
            Component: Protected,
          },
          {
            path: 'cart',
            Component: CartPage,
          },
          {
            path: 'orders',
            Component: Orders,
            children: [
              {
                index: true,
                Component: () => <h1>Orders index page under construction</h1>,
              },
              {
                path: ':id',
                Component: OrderById,
              },
            ],
          },
        ],
      },
    ],
  },
  {
    Component: RequireFromCart,
    ErrorBoundary: DefaultErrorBoundary,
    children: [
      {
        path: 'checkout',
        Component: CheckoutPage,
      },
    ],
  },
  {
    path: '/login',
    Component: SignInSide,
    ErrorBoundary: DefaultErrorBoundary,
  },
  {
    path: '/signup',
    Component: SignUp,
    ErrorBoundary: DefaultErrorBoundary,
  },
  // NB Only `/api/v1/*` will be subject to reverse proxy
  {
    path: '*',
    Component: NotFound,
  },
];

const router = createBrowserRouter(routes);

export default router;
