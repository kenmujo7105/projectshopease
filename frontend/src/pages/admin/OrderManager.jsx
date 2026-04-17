import { useState, useEffect } from 'react';
import toast from 'react-hot-toast';
import orderApi from '../../api/orderApi';
import { formatCurrency, formatDate, getStatusBadgeClass, getStatusLabel } from '../../utils/helpers';

export default function OrderManager() {
  const [orders, setOrders] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  const fetchOrders = async () => { setIsLoading(true); try { const res = await orderApi.getAll(); setOrders(res.data || []); } catch {/* skip */} finally { setIsLoading(false); } };
  useEffect(() => { fetchOrders(); }, []);

  const handleUpdateStatus = async (orderId, status) => {
    try {
      await orderApi.updateStatus(orderId, { status });
      toast.success('Cập nhật trạng thái thành công');
      fetchOrders();
    } catch { toast.error('Lỗi cập nhật trạng thái'); }
  };

  const statuses = ['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

  return (
    <div className="animate-fade-in">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Quản lý đơn hàng</h1>
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead className="bg-gray-50">
              <tr>
                <th className="text-left px-4 py-3">Mã đơn</th>
                <th className="text-left px-4 py-3">Ngày tạo</th>
                <th className="text-left px-4 py-3">Sản phẩm</th>
                <th className="text-left px-4 py-3">Tổng tiền</th>
                <th className="text-left px-4 py-3">Trạng thái</th>
                <th className="text-left px-4 py-3">Cập nhật</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {orders.map(o => (
                <tr key={o.id} className="hover:bg-gray-50">
                  <td className="px-4 py-3 font-mono text-xs text-gray-600">{o.id?.substring(0, 8)}...</td>
                  <td className="px-4 py-3 text-gray-500 text-xs">{formatDate(o.createdAt)}</td>
                  <td className="px-4 py-3 text-gray-500">{o.orderItems?.length || 0} sản phẩm</td>
                  <td className="px-4 py-3 text-primary-500 font-medium">{formatCurrency(o.total)}</td>
                  <td className="px-4 py-3"><span className={`badge ${getStatusBadgeClass(o.status)}`}>{getStatusLabel(o.status)}</span></td>
                  <td className="px-4 py-3">
                    <select
                      value={o.status}
                      onChange={e => handleUpdateStatus(o.id, e.target.value)}
                      className="input-field text-xs py-1 px-2 w-36"
                    >
                      {statuses.map(s => <option key={s} value={s}>{getStatusLabel(s)}</option>)}
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {orders.length === 0 && !isLoading && <p className="text-center text-gray-400 py-8">Chưa có đơn hàng nào</p>}
      </div>
    </div>
  );
}
