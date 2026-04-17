import { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Trash2, Minus, Plus, ShoppingBag, ArrowLeft } from 'lucide-react';
import toast from 'react-hot-toast';
import useCartStore from '../store/useCartStore';
import useAuthStore from '../store/useAuthStore';
import LoadingSpinner from '../components/common/LoadingSpinner';
import EmptyState from '../components/common/EmptyState';
import { formatCurrency } from '../utils/helpers';

export default function Cart() {
  const { items, totalPrice, totalQuantity, isLoading, fetchCart, updateItem, removeItem } = useCartStore();
  const { isAuthenticated } = useAuthStore();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) fetchCart();
  }, [isAuthenticated, fetchCart]);

  const handleUpdateQuantity = async (item, newNum) => {
    if (newNum < 1) return;
    try {
      await updateItem(item.detail?.id, item.detail?.id, newNum);
    } catch {
      toast.error('Lỗi cập nhật số lượng');
    }
  };

  const handleRemove = async (item) => {
    try {
      await removeItem(item.detail?.id);
      toast.success('Đã xóa sản phẩm khỏi giỏ hàng');
    } catch {
      toast.error('Lỗi xóa sản phẩm');
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-16 text-center">
        <ShoppingBag className="w-16 h-16 text-gray-300 mx-auto mb-4" />
        <h2 className="text-xl font-bold text-gray-700 mb-2">Vui lòng đăng nhập</h2>
        <p className="text-gray-400 mb-6">Đăng nhập để xem giỏ hàng của bạn</p>
        <Link to="/login" className="btn-primary">Đăng nhập</Link>
      </div>
    );
  }

  if (isLoading) return <LoadingSpinner className="py-32" size="lg" />;

  if (items.length === 0) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-16">
        <EmptyState title="Giỏ hàng trống" description="Hãy thêm sản phẩm vào giỏ hàng" icon={ShoppingBag} />
        <div className="text-center mt-6">
          <Link to="/products" className="btn-primary inline-flex items-center gap-2">
            <ArrowLeft className="w-4 h-4" /> Tiếp tục mua sắm
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto px-4 py-6 animate-fade-in">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Giỏ hàng ({totalQuantity} sản phẩm)</h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Cart Items */}
        <div className="lg:col-span-2 space-y-3">
          {items.map((cartItem, idx) => (
            <div key={idx} className="bg-white rounded-xl p-4 shadow-sm flex gap-4">
              <img
                src={cartItem.item?.imageUrl || 'https://via.placeholder.com/100'}
                alt={cartItem.item?.name}
                className="w-20 h-20 sm:w-24 sm:h-24 object-cover rounded-lg flex-shrink-0"
              />
              <div className="flex-1 min-w-0">
                <Link to={`/product/${cartItem.item?.id}`} className="text-sm font-medium text-gray-800 hover:text-primary-500 line-clamp-2">
                  {cartItem.item?.name}
                </Link>
                <p className="text-xs text-gray-400 mt-0.5">Phân loại: {cartItem.detail?.info}</p>
                <p className="text-primary-500 font-bold mt-1">{formatCurrency(cartItem.detail?.price)}</p>

                <div className="flex items-center justify-between mt-2">
                  <div className="flex items-center border rounded-lg">
                    <button onClick={() => handleUpdateQuantity(cartItem, cartItem.num - 1)} className="p-1.5 hover:bg-gray-50">
                      <Minus className="w-3.5 h-3.5" />
                    </button>
                    <span className="px-3 text-sm font-medium">{cartItem.num}</span>
                    <button onClick={() => handleUpdateQuantity(cartItem, cartItem.num + 1)} className="p-1.5 hover:bg-gray-50">
                      <Plus className="w-3.5 h-3.5" />
                    </button>
                  </div>
                  <button onClick={() => handleRemove(cartItem)} className="text-gray-400 hover:text-red-500 p-1.5 transition-colors">
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Summary */}
        <div className="bg-white rounded-xl p-6 shadow-sm h-fit sticky top-20">
          <h3 className="font-bold text-gray-800 mb-4">Tóm tắt đơn hàng</h3>
          <div className="space-y-2 text-sm">
            <div className="flex justify-between text-gray-500">
              <span>Tạm tính ({totalQuantity} sản phẩm)</span>
              <span>{formatCurrency(totalPrice)}</span>
            </div>
            <div className="flex justify-between text-gray-500">
              <span>Phí vận chuyển</span>
              <span className="text-green-500">Tính khi đặt hàng</span>
            </div>
            <hr className="my-3" />
            <div className="flex justify-between text-lg font-bold text-gray-900">
              <span>Tổng cộng</span>
              <span className="text-primary-500">{formatCurrency(totalPrice)}</span>
            </div>
          </div>
          <button
            onClick={() => navigate('/checkout')}
            className="btn-primary w-full mt-6"
          >
            Tiến hành đặt hàng
          </button>
          <Link to="/products" className="block text-center text-sm text-primary-500 hover:underline mt-3">
            Tiếp tục mua sắm
          </Link>
        </div>
      </div>
    </div>
  );
}
