import { useState, useEffect } from 'react';
import { Package, FolderTree, ClipboardList, Users, TrendingUp } from 'lucide-react';
import productApi from '../../api/productApi';
import categoryApi from '../../api/categoryApi';
import orderApi from '../../api/orderApi';
import userApi from '../../api/userApi';

export default function Dashboard() {
  const [stats, setStats] = useState({ products: 0, categories: 0, orders: 0, users: 0, revenue: 0 });

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [prodRes, catRes, orderRes, userRes] = await Promise.allSettled([
          productApi.getAll({ page: 0, size: 1 }),
          categoryApi.getAll(),
          orderApi.getAll(),
          userApi.getAll(),
        ]);
        const orders = orderRes.status === 'fulfilled' ? (orderRes.value.data || []) : [];
        const revenue = orders.filter(o => o.status === 'DELIVERED').reduce((sum, o) => sum + (o.total || 0), 0);
        setStats({
          products: prodRes.status === 'fulfilled' ? (prodRes.value.data?.length || 0) : 0,
          categories: catRes.status === 'fulfilled' ? (catRes.value.data?.length || 0) : 0,
          orders: orders.length,
          users: userRes.status === 'fulfilled' ? (userRes.value.data?.length || 0) : 0,
          revenue,
        });
      } catch {/* skip */}
    };
    fetchStats();
  }, []);

  const cards = [
    { label: 'Sản phẩm', value: stats.products, icon: Package, color: 'bg-blue-500', bg: 'bg-blue-50' },
    { label: 'Danh mục', value: stats.categories, icon: FolderTree, color: 'bg-emerald-500', bg: 'bg-emerald-50' },
    { label: 'Đơn hàng', value: stats.orders, icon: ClipboardList, color: 'bg-orange-500', bg: 'bg-orange-50' },
    { label: 'Người dùng', value: stats.users, icon: Users, color: 'bg-purple-500', bg: 'bg-purple-50' },
  ];

  return (
    <div className="animate-fade-in">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Dashboard</h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        {cards.map(c => (
          <div key={c.label} className="bg-white rounded-xl p-5 shadow-sm hover:shadow-md transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-400">{c.label}</p>
                <p className="text-3xl font-extrabold text-gray-800 mt-1">{c.value}</p>
              </div>
              <div className={`w-12 h-12 ${c.bg} rounded-xl flex items-center justify-center`}>
                <c.icon className={`w-6 h-6 text-${c.color.replace('bg-', '')}`} style={{ color: c.color === 'bg-blue-500' ? '#3b82f6' : c.color === 'bg-emerald-500' ? '#10b981' : c.color === 'bg-orange-500' ? '#f97316' : '#a855f7' }} />
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Revenue Card */}
      <div className="bg-gradient-to-r from-primary-500 to-primary-600 rounded-xl p-6 text-white shadow-lg">
        <div className="flex items-center gap-3 mb-2">
          <TrendingUp className="w-6 h-6" />
          <h2 className="text-lg font-semibold">Tổng doanh thu (đơn đã giao)</h2>
        </div>
        <p className="text-4xl font-extrabold">{new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(stats.revenue)}</p>
      </div>
    </div>
  );
}
