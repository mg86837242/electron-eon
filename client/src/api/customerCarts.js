import { authApi } from '../lib/axiosConfig';

export async function getCartById({ params }) {
  const response = await authApi.get(`/customer/carts/${params.id}`);
  return response.data;
}

export const getCartsForCurrUser = async () => {
  const response = await authApi.get(`/customer/carts/current-user`);
  return response.data;
};

// For catalog page
export async function upsertProdQtyInCartForCurrUser({ request }) {
  const submitted = await request.json();
  const body = {};
  const response = await authApi.post(
    `/customer/carts/current-user/products/${submitted.productId}/upsert-quantity?quantity=${submitted.quantity}`,
    body,
  );
  return;
}

// For catalog page
export const incrementProdQtyInCartForCurrUser = productId =>
  authApi.patch(
    `/customer/carts/current-user/products/${productId}/increment-quantity`,
    {},
  );

// For catalog page
export const decrementProdQtyInCartForCurrUser = productId =>
  authApi.patch(
    `/customer/carts/current-user/products/${productId}/decrement-quantity`,
    {},
  );

// For individual product page
export async function updateProdQtyInCartById({ request }) {
  const submitted = await request.json();
  const body = {};
  const response = await authApi.patch(
    `/customer/carts/${submitted.id}/update-quantity?quantity=${submitted.quantity}`,
    body,
  );
  return;
}

// For dedicated cart page or admin page
export const incrementProdQtyInCartById = id =>
  authApi.patch(`/customer/carts/${id}/increment-quantity`, {});

// For dedicated cart page or admin page
export const decrementProdQtyInCartById = id =>
  authApi.patch(`/customer/carts/${id}/decrement-quantity`, {});

export const deleteCartById = id => authApi.delete(`/customer/carts/${id}`);

export async function deleteCartForCurrUser() {
  const response = await authApi.delete(`/customer/carts/current-user`);
  return;
}
