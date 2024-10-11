import { authApi } from '../lib/axiosConfig';

export async function getAllOrders() {
  const response = await authApi.get(`/admin/orders`);
  return response.data;
}

export async function getOrderById({ params }) {
  const response = await authApi.get(`/admin/orders/${params.id}`);
  return response.data;
}

export async function getOrdersByUserId({ params }) {
  const response = await authApi.get(
    `/admin/orders/by-user-id/${params.userId}`,
  );
  return response.data;
}

export async function updateOrderById({ params, request }) {
  const body = await request.json();
  const response = await authApi.patch(`/admin/orders/${params.id}`, body);
  return;
}

export async function deleteOrderById({ params }) {
  const response = await authApi.delete(`/admin/orders/${params.id}`);
  return;
}
