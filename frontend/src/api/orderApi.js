import axiosClient from './axiosClient';

const orderApi = {
  create: (data) => axiosClient.post('/order', data),
  preview: (data) => axiosClient.post('/order/preview', data),
  getById: (id) => axiosClient.get(`/order/${id}`),
  getAll: () => axiosClient.get('/order/all'),
  getStatusList: () => axiosClient.get('/order/status'),
  updateStatus: (orderId, data) => axiosClient.patch(`/order/${orderId}/update-status`, data),
  delete: (orderId) => axiosClient.delete(`/order/${orderId}`),
};

export default orderApi;
