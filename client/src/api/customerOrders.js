import { authApi } from '../lib/axiosConfig';

export async function getOrdersForCurrUser() {
  const response = await authApi.get(`/customer/orders/current-user`);
  return response;
}

export const getOrderByIdForCurrUser = async id => {
  const response = await authApi.get(`/customer/orders/${id}`);
  return response.data;
};

export const addOrderForCurrUser = async orderData => {
  const response = await authApi.post(
    `/customer/orders/current-user`,
    orderData,
  );
  return response.data;
};
