import { authApi } from '../lib/axiosConfig';

export async function getAllUsers() {
  const response = await authApi.get(`/admin/users`);
  return response.data;
}

export async function getUserById({ params }) {
  const response = await authApi.get(`/admin/users/${params.id}`);
  return response.data;
}

export async function updateUserById({ params, request }) {
  const body = await request.json();
  const response = await authApi.patch(`/admin/users/${params.id}`, body);
  return;
}

export async function deleteUserById({ params }) {
  const response = await authApi.delete(`/admin/users/${params.id}`);
  return;
}
