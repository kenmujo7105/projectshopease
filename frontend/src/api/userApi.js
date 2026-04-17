import axiosClient from './axiosClient';

const userApi = {
  getAll: () => axiosClient.get('/user'),
  getById: (id) => axiosClient.get(`/user/${id}`),
  update: (id, data) => axiosClient.put(`/user/${id}`, data),
  updatePassword: (id, data) => axiosClient.put(`/user/${id}/password`, data),
  delete: (id) => axiosClient.delete(`/user/${id}`),
  getAddresses: (userId) => axiosClient.get(`/user/${userId}/address`),
  createAddress: (userId, data) => axiosClient.post(`/user/${userId}/address`, data),
  updateAddress: (userId, addressId, data) => axiosClient.put(`/user/${userId}/address/${addressId}`, data),
  deleteAddress: (userId, addressId) => axiosClient.delete(`/user/${userId}/address/${addressId}`),
};

export default userApi;
