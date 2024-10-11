import * as React from 'react';

import { Spinner } from '../../components';
import useAuthUser from '../../hooks/useAuthUser';
import useAuthStore from '../../store/useAuthStore';
import isTokenExpired from '../../utils/isTokenExpired';

import ProductCatalog from './ProductCatalog';
import ProductCatalogForCurrUser from './ProductCatalogForCurrUser';

export default function ProductsPage() {
  const token = useAuthStore(state => state.token);
  const { isAuthUserPending } = useAuthUser();

  // `token` state is persisted, thus can be immediately used to tell if the user needs to be redirected
  const hasPersistedToken = !!token && !isTokenExpired(token);

  if (!hasPersistedToken) {
    return <ProductCatalog />;
  }

  if (isAuthUserPending) {
    return <Spinner />;
  }

  return <ProductCatalogForCurrUser />;
}
