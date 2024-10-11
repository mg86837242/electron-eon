import { authApi } from '../lib/axiosConfig';

export async function getCurrUser() {
  const response = await authApi.get(`/admin/users/current-user`);
  return response.data;
}

export async function updateUserById({ request }) {
  const body = await request.json();
  const response = await authApi.patch(`/admin/users/current-user`, body);
  return;
}

export async function deleteUserById() {
  const response = await authApi.delete(`/admin/users/current-user`);
  return;
}
