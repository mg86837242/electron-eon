import { authApi } from '../lib/axiosConfig';

export async function getOrderProductById({ params }) {
  const response = await authApi.get(`/customer/order-products/${params.id}`);
  return response.data;
}

export async function getOrderProductByOrderId({ params }) {
  const response = await authApi.get(
    `/customer/order-products/by-order-id/${params.orderId}`,
  );
  return response.data;
}
