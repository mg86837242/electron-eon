import { authApi } from '../lib/axiosConfig';

export async function addProduct({ request }) {
  const body = await request.json();
  const response = await authApi.post(`/admin/products`, body);
  return;
}

export async function updateProductById({ params, request }) {
  const body = await request.json();
  const response = await authApi.patch(`/admin/products/${params.id}`, body);
  return;
}

export async function deleteProductById({ params }) {
  const response = await authApi.delete(`/admin/products/${params.id}`);
  return;
}
