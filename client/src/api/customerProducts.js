import { authApi } from '../lib/axiosConfig';

export const getAllProdsForCurrUser = async () => {
  const response = await authApi.get(`/customer/products`);
  return response.data;
};

export const getProdByIdForCurrUser = async id => {
  const response = await authApi.get(`/customer/products/${id}`);
  return response.data;
};

export const getProdsByCategoryForCurrUser = async category => {
  const response = await authApi.get(`/customer/products?category=${category}`);
  return response.data;
};
