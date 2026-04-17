import { create } from 'zustand';
import cartApi from '../api/cartApi';

const useCartStore = create((set, get) => ({
  cart: null,
  items: [],
  totalPrice: 0,
  totalQuantity: 0,
  isLoading: false,

  fetchCart: async () => {
    set({ isLoading: true });
    try {
      const res = await cartApi.getMyCart();
      const cart = res.data;
      set({
        cart,
        items: cart?.cartItems || [],
        totalPrice: cart?.totalPrice || 0,
        totalQuantity: cart?.totalQuantity || 0,
      });
    } catch {
      set({ cart: null, items: [], totalPrice: 0, totalQuantity: 0 });
    } finally {
      set({ isLoading: false });
    }
  },

  addItem: async (productId, detailId, num = 1) => {
    const res = await cartApi.addItem({ productId, detailId, num });
    await get().fetchCart();
    return res;
  },

  updateItem: async (itemId, detailId, num) => {
    const res = await cartApi.updateItem(itemId, { detailId, num });
    await get().fetchCart();
    return res;
  },

  removeItem: async (itemId) => {
    await cartApi.deleteItem(itemId);
    await get().fetchCart();
  },

  clearCart: () => set({ cart: null, items: [], totalPrice: 0, totalQuantity: 0 }),
}));

export default useCartStore;
