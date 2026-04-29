import axiosClient from './axiosClient';

const productApi = {
  getAll: (params) => axiosClient.get('/product', { params }),
  getByCategory: (name) => axiosClient.get('/product/by-category', { params: { name } }),
  getByBrand: (brandId) => axiosClient.get('/product/by-brand', { params: { brandId } }),
  getById: (id) => axiosClient.get(`/product/${id}`),
  search: (keyword) => axiosClient.get('/product/search', { params: { keyword } }),
  suggest: (keyword) => axiosClient.get('/product/suggest', { params: { keyword } }),
  create: (data) => axiosClient.post('/product', data),
  addDetail: (productId, data) => axiosClient.post(`/product/${productId}/detail`, data),
  updateInfo: (productId, data) => axiosClient.put(`/product/${productId}/info`, data),
  updateDetail: (productId, data) => axiosClient.put(`/product/${productId}/detail`, data),
  delete: (productId) => axiosClient.delete(`/product/${productId}`),
  deleteDetail: (productId, detailId) => axiosClient.delete(`/product/${productId}/detail/${detailId}`),
};

export default productApi;
