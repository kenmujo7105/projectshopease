import axiosClient from './axiosClient';

const chatApi = {
  send: (message) => axiosClient.post('/chat', { message }),
};

export default chatApi;
