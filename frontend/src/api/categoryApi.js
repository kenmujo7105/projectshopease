import axiosClient from './axiosClient';

const categoryApi = {
  getAll: () => axiosClient.get('/category/all'),
  create: (data) => axiosClient.post('/category', data),
  update: (id, data) => axiosClient.put(`/category/${id}`, data),
  delete: (id) => axiosClient.delete(`/category/${id}`),
};

export default categoryApi;
