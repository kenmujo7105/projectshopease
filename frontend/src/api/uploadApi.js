import axiosClient from './axiosClient';

const uploadApi = {
  upload: (file, fileType, ownerType, ownerId) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('fileType', fileType);
    formData.append('ownerType', ownerType);
    formData.append('ownerId', ownerId);
    return axiosClient.post('/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
};

export default uploadApi;
