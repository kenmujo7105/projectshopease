import axiosClient from './axiosClient';

const brandApi = {
  getAll: () => axiosClient.get('/brand/all'),
  create: (data) => axiosClient.post('/brand', data),
  update: (id, data) => axiosClient.put(`/brand/${id}`, data),
  delete: (id) => axiosClient.delete(`/brand/${id}`),
};

export default brandApi;
