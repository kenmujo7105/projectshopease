import { Outlet, Link } from 'react-router-dom';
import { LogOut, Home } from 'lucide-react';
import AdminSidebar from './AdminSidebar';
import useAuthStore from '../../store/useAuthStore';

export default function AdminLayout() {
  const { user, logout } = useAuthStore();

  return (
    <div className="flex min-h-screen bg-gray-50">
      <AdminSidebar />
      <div className="flex-1 flex flex-col">
        {/* Admin Top Bar */}
        <header className="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-6 sticky top-0 z-40">
          <h2 className="text-lg font-semibold text-gray-800">Quản trị hệ thống</h2>
          <div className="flex items-center gap-3">
            <Link to="/" className="flex items-center gap-1.5 text-sm text-gray-500 hover:text-primary-500 transition-colors">
              <Home className="w-4 h-4" /> Về trang chủ
            </Link>
            <span className="text-sm text-gray-400">|</span>
            <span className="text-sm text-gray-600">{user?.firstName} {user?.lastName}</span>
            <button 
              onClick={logout}
              className="p-2 text-gray-400 hover:text-red-500 rounded-lg hover:bg-gray-100 transition-colors"
            >
              <LogOut className="w-4 h-4" />
            </button>
          </div>
        </header>
        <main className="flex-1 p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
