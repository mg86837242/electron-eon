import axios from 'axios';

import BASE_URL from '../constants/urls';

export const authApi = axios.create({
  baseURL: BASE_URL,
  timeout: 5_000,
});

authApi.interceptors.request.use(
  config => {
    const authStore = window.localStorage.getItem(
      `${import.meta.env.VITE_APP_NAME}-auth`,
    );
    const authStoreObj = JSON.parse(authStore);
    const token = authStoreObj?.state?.token;

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  error => {
    return Promise.reject(error);
  },
);

export const api = axios.create({
  baseURL: BASE_URL,
  timeout: 5_000,
});
