import { create } from 'zustand';
import authApi from '../api/authApi';

const useAuthStore = create((set, get) => ({
  user: null,
  isAuthenticated: !!localStorage.getItem('accessToken'),
  isLoading: false,

  login: async (email, password, role = 'USER') => {
    set({ isLoading: true });
    try {
      const res = await authApi.login({ email, password, role });
      const { accessToken, refreshToken } = res.data;
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      set({ isAuthenticated: true });
      await get().fetchProfile();
      return res;
    } finally {
      set({ isLoading: false });
    }
  },

  fetchProfile: async () => {
    try {
      const res = await authApi.getProfile();
      set({ user: res.data, isAuthenticated: true });
      return res.data;
    } catch {
      set({ user: null, isAuthenticated: false });
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
    }
  },

  logout: async () => {
    const token = localStorage.getItem('accessToken');
    try {
      if (token) await authApi.logout({ token });
    } catch {
      // ignore
    } finally {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      set({ user: null, isAuthenticated: false });
    }
  },

  isAdmin: () => get().user?.role === 'ADMIN',
}));

export default useAuthStore;
