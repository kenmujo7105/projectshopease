import axiosClient from './axiosClient';

const authApi = {
  login: (data) => axiosClient.post('/auth/login', data),
  register: (data) => axiosClient.post('/auth/register', data),
  verifyOtp: (data) => axiosClient.post('/auth/register/verify-otp', data),
  resendOtp: (data) => axiosClient.post('/auth/register/resend-otp', data),
  getProfile: () => axiosClient.get('/auth/profile'),
  refreshToken: (data) => axiosClient.post('/auth/refresh-token', data),
  logout: (data) => axiosClient.post('/auth/logout', data),
  forgotPassword: (data) => axiosClient.post('/auth/forgot-password', data),
  forgotVerifyOtp: (data) => axiosClient.post('/auth/forgot-password/verify-otp', data),
  forgotChangePassword: (data) => axiosClient.post('/auth/forgot-password/change', data),
  forgotResendOtp: (data) => axiosClient.post('/auth/forgot-password/resend-otp', data),
};

export default authApi;
