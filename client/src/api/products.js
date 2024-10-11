import { api } from '../lib/axiosConfig';

export const getAllProducts = async () => {
  const response = await api.get(`/products`);
  return response.data;
};

export const getProductById = async id => {
  const response = await api.get(`/products/${id}`);
  return response.data;
};

export const getProductsByCategory = async category => {
  const response = await api.get(`/products?category=${category}`);
  return response.data;
};
