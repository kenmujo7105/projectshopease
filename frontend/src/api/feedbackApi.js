import axiosClient from './axiosClient';

const feedbackApi = {
  getByProduct: (productId, params) => axiosClient.get(`/feedback/${productId}`, { params }),
  create: (productId, data) => axiosClient.post(`/feedback/${productId}`, data),
  update: (feedbackId, data) => axiosClient.put(`/feedback/${feedbackId}`, data),
  delete: (feedbackId) => axiosClient.delete(`/feedback/${feedbackId}`),
};

export default feedbackApi;
