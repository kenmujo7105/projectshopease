import axiosClient from './axiosClient';

const cartApi = {
  getMyCart: () => axiosClient.get('/cart/me'),
  addItem: (data) => axiosClient.post('/cart-item', data),
  updateItem: (itemId, data) => axiosClient.put(`/cart-item/${itemId}`, data),
  deleteItem: (itemId) => axiosClient.delete(`/cart-item/${itemId}`),
};

export default cartApi;
