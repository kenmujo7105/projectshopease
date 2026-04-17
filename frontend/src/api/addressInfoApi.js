import axiosClient from './axiosClient';

const addressInfoApi = {
  getProvinces: () => axiosClient.get('/address/province'),
  getDistricts: (provinceId) => axiosClient.get(`/address/${provinceId}/district`),
  getWards: (districtId) => axiosClient.get(`/address/${districtId}/ward`),
};

export default addressInfoApi;
