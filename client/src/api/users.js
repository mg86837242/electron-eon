import { api } from '../lib/axiosConfig';

export const getAuthUser = async (token, cancelToken) => {
  const requestOptions = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
    cancelToken,
  };

  const response = await api.get(`/auth/me`, requestOptions);

  return response.data;
};

export const loginUser = async ({ username, password }) => {
  const requestOptions = {
    auth: {
      username,
      password,
    },
  };

  const response = await api.post(`/auth/login`, {}, requestOptions);

  return response.data;
};

export const registerUser = registrationData =>
  api.post(`/users/register`, registrationData);
