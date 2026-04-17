import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import toast from 'react-hot-toast';
import orderApi from '../api/orderApi';
import LoadingSpinner from '../components/common/LoadingSpinner';
import { formatCurrency, formatDate, getStatusBadgeClass, getStatusLabel } from '../utils/helpers';

export default function OrderDetail() {
  const { id } = useParams();
  const [order, setOrder] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetch = async () => {
      try {
        const res = await orderApi.getById(id);
        setOrder(res.data);
      } catch {
        toast.error('Không tìm thấy đơn hàng');
      } finally {
        setIsLoading(false);
      }
    };
    fetch();
  }, [id]);

  if (isLoading) return <LoadingSpinner className="py-32" size="lg" />;
  if (!order) return <div className="text-center py-32 text-gray-400">Đơn hàng không tồn tại</div>;

  const statuses = ['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED'];
  const currentIdx = statuses.indexOf(order.status);

  return (
    <div className="max-w-4xl mx-auto px-4 py-6 animate-fade-in">
      <Link to="/orders" className="flex items-center gap-1 text-sm text-gray-500 hover:text-primary-500 mb-4">
        <ArrowLeft className="w-4 h-4" /> Quay lại
      </Link>

      <div className="bg-white rounded-xl shadow-sm p-6 mb-6">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h1 className="text-xl font-bold text-gray-800">Chi tiết đơn hàng</h1>
            <p className="text-sm text-gray-400 mt-0.5">Mã: {order.id}</p>
            <p className="text-sm text-gray-400">{formatDate(order.createdAt)}</p>
          </div>
          <span className={`badge text-sm ${getStatusBadgeClass(order.status)}`}>{getStatusLabel(order.status)}</span>
        </div>

        {/* Status Timeline */}
        {order.status !== 'CANCELLED' && (
          <div className="flex items-center justify-between mt-6 mb-2 px-4">
            {statuses.map((s, i) => (
              <div key={s} className="flex items-center flex-1 last:flex-none">
                <div className={`w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold ${
                  i <= currentIdx ? 'bg-primary-500 text-white' : 'bg-gray-200 text-gray-400'
                }`}>
                  {i + 1}
                </div>
                {i < statuses.length - 1 && (
                  <div className={`flex-1 h-1 mx-1 rounded ${i < currentIdx ? 'bg-primary-500' : 'bg-gray-200'}`} />
                )}
              </div>
            ))}
          </div>
        )}
        {order.status !== 'CANCELLED' && (
          <div className="flex justify-between text-[10px] text-gray-400 px-2">
            {statuses.map(s => <span key={s}>{getStatusLabel(s)}</span>)}
          </div>
        )}
      </div>

      {/* Items */}
      <div className="bg-white rounded-xl shadow-sm p-6">
        <h2 className="font-bold text-gray-800 mb-4">Sản phẩm</h2>
        <div className="space-y-3">
          {order.orderItems?.map(item => (
            <div key={item.id} className="flex items-center gap-4 py-3 border-b last:border-0">
              <div className="w-16 h-16 bg-gray-100 rounded-lg flex-shrink-0" />
              <div className="flex-1">
                <p className="text-sm font-medium text-gray-800">{item.item?.info || 'Sản phẩm'}</p>
                <p className="text-xs text-gray-400">x{item.num}</p>
              </div>
              <span className="text-sm font-medium text-primary-500">{formatCurrency(item.item?.price * item.num)}</span>
            </div>
          ))}
        </div>
        <div className="flex justify-end mt-4 pt-4 border-t">
          <span className="text-lg font-bold text-gray-800">Tổng: <span className="text-primary-500">{formatCurrency(order.total)}</span></span>
        </div>
      </div>
    </div>
  );
}
