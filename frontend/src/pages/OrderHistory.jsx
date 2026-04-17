import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Package, Eye } from 'lucide-react';
import orderApi from '../api/orderApi';
import LoadingSpinner from '../components/common/LoadingSpinner';
import EmptyState from '../components/common/EmptyState';
import { formatCurrency, formatDate, getStatusBadgeClass, getStatusLabel } from '../utils/helpers';

export default function OrderHistory() {
  const [orders, setOrders] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const res = await orderApi.getAll();
        setOrders(res.data || []);
      } catch {
        setOrders([]);
      } finally {
        setIsLoading(false);
      }
    };
    fetchOrders();
  }, []);

  if (isLoading) return <LoadingSpinner className="py-32" size="lg" />;

  return (
    <div className="max-w-4xl mx-auto px-4 py-6 animate-fade-in">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Đơn hàng của tôi</h1>

      {orders.length === 0 ? (
        <EmptyState title="Chưa có đơn hàng" description="Bắt đầu mua sắm ngay" icon={Package} />
      ) : (
        <div className="space-y-4">
          {orders.map(order => (
            <div key={order.id} className="bg-white rounded-xl shadow-sm p-5 hover:shadow-md transition-shadow">
              <div className="flex items-center justify-between mb-3">
                <div>
                  <p className="text-sm text-gray-400">Mã đơn: <span className="font-mono text-gray-600">{order.id?.substring(0, 8)}...</span></p>
                  <p className="text-xs text-gray-400 mt-0.5">{formatDate(order.createdAt)}</p>
                </div>
                <div className="flex items-center gap-3">
                  <span className={`badge ${getStatusBadgeClass(order.status)}`}>{getStatusLabel(order.status)}</span>
                  <Link to={`/orders/${order.id}`} className="text-primary-500 hover:text-primary-600">
                    <Eye className="w-4.5 h-4.5" />
                  </Link>
                </div>
              </div>
              {order.orderItems?.slice(0, 2).map(item => (
                <div key={item.id} className="flex items-center gap-3 py-2 border-t border-gray-50">
                  <div className="w-12 h-12 bg-gray-100 rounded" />
                  <div className="flex-1">
                    <p className="text-sm text-gray-700">{item.item?.info || 'Sản phẩm'}</p>
                    <p className="text-xs text-gray-400">x{item.num}</p>
                  </div>
                </div>
              ))}
              {order.orderItems?.length > 2 && (
                <p className="text-xs text-gray-400 mt-1">+{order.orderItems.length - 2} sản phẩm khác</p>
              )}
              <div className="flex justify-end mt-3 pt-3 border-t border-gray-100">
                <span className="text-sm text-gray-500">Tổng: <strong className="text-primary-500 text-base">{formatCurrency(order.total)}</strong></span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
