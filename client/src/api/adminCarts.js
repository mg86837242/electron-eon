import { authApi } from '../lib/axiosConfig';

export async function getCartsByUserId({ params }) {
  const response = await authApi.get(
    `/admin/carts/by-user-id/${params.userId}`,
  );
  return response.data;
}

export async function deleteCartsByUserId({ params }) {
  const response = await authApi.delete(
    `/admin/carts/by-user-id/${params.userId}`,
  );
  return response;
}
