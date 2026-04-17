import { NavLink } from 'react-router-dom';
import { LayoutDashboard, Package, FolderTree, Tag, ClipboardList, Users } from 'lucide-react';

const links = [
  { to: '/admin', icon: LayoutDashboard, label: 'Dashboard', end: true },
  { to: '/admin/products', icon: Package, label: 'Sản phẩm' },
  { to: '/admin/categories', icon: FolderTree, label: 'Danh mục' },
  { to: '/admin/brands', icon: Tag, label: 'Thương hiệu' },
  { to: '/admin/orders', icon: ClipboardList, label: 'Đơn hàng' },
  { to: '/admin/users', icon: Users, label: 'Người dùng' },
];

export default function AdminSidebar() {
  return (
    <aside className="w-64 min-h-screen bg-dark-900 text-gray-300 flex-shrink-0 hidden lg:block">
      <div className="p-6">
        <div className="flex items-center gap-2 mb-8">
          <div className="w-9 h-9 bg-primary-500 rounded-lg flex items-center justify-center">
            <span className="text-white font-extrabold text-lg">S</span>
          </div>
          <div>
            <span className="text-white font-bold text-lg">ShopEase</span>
            <p className="text-xs text-gray-500">Admin Panel</p>
          </div>
        </div>
        <nav className="space-y-1">
          {links.map(({ to, icon: Icon, label, end }) => (
            <NavLink
              key={to}
              to={to}
              end={end}
              className={({ isActive }) =>
                `flex items-center gap-3 px-4 py-2.5 rounded-lg text-sm font-medium transition-all duration-200 ${
                  isActive
                    ? 'bg-primary-500/10 text-primary-400'
                    : 'hover:bg-white/5 text-gray-400 hover:text-gray-200'
                }`
              }
            >
              <Icon className="w-4.5 h-4.5" />
              {label}
            </NavLink>
          ))}
        </nav>
      </div>
    </aside>
  );
}
