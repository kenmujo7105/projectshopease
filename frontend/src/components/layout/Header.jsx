import { useState, useEffect, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Search, ShoppingCart, User, LogOut, Package, ChevronDown, Menu, X, LayoutDashboard } from 'lucide-react';
import useAuthStore from '../../store/useAuthStore';
import useCartStore from '../../store/useCartStore';

export default function Header() {
  const { user, isAuthenticated, logout } = useAuthStore();
  const { totalQuantity } = useCartStore();
  const [searchQuery, setSearchQuery] = useState('');
  const [showUserMenu, setShowUserMenu] = useState(false);
  const [showMobileMenu, setShowMobileMenu] = useState(false);
  const navigate = useNavigate();
  const menuRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) setShowUserMenu(false);
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/products?keyword=${encodeURIComponent(searchQuery.trim())}`);
      setSearchQuery('');
    }
  };

  const handleLogout = async () => {
    await logout();
    setShowUserMenu(false);
    navigate('/');
  };

  return (
    <header className="bg-gradient-to-r from-primary-500 to-primary-600 text-white sticky top-0 z-50 shadow-lg">
      <div className="max-w-7xl mx-auto px-4">
        {/* Top bar */}
        <div className="flex items-center justify-between h-16 gap-4">
          {/* Logo */}
          <Link to="/" className="flex-shrink-0 flex items-center gap-2">
            <div className="w-8 h-8 bg-white rounded-lg flex items-center justify-center">
              <span className="text-primary-500 font-extrabold text-lg">S</span>
            </div>
            <span className="hidden sm:block text-xl font-bold tracking-tight">ShopEase</span>
          </Link>

          {/* Search Bar */}
          <form onSubmit={handleSearch} className="flex-1 max-w-xl hidden sm:flex">
            <div className="relative w-full">
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Tìm kiếm sản phẩm..."
                className="w-full py-2 pl-4 pr-12 rounded-lg text-gray-800 text-sm focus:outline-none focus:ring-2 focus:ring-white/50"
              />
              <button type="submit" className="absolute right-0 top-0 h-full px-4 bg-primary-600 hover:bg-primary-700 rounded-r-lg transition-colors">
                <Search className="w-4 h-4" />
              </button>
            </div>
          </form>

          {/* Right Actions */}
          <div className="flex items-center gap-2 sm:gap-3">
            {/* Cart */}
            <Link to="/cart" className="relative p-2 hover:bg-white/10 rounded-lg transition-colors" id="cart-icon">
              <ShoppingCart className="w-5 h-5" />
              {totalQuantity > 0 && (
                <span className="absolute -top-0.5 -right-0.5 bg-yellow-400 text-gray-900 text-xs font-bold rounded-full w-5 h-5 flex items-center justify-center">
                  {totalQuantity > 99 ? '99+' : totalQuantity}
                </span>
              )}
            </Link>

            {/* User */}
            {isAuthenticated ? (
              <div className="relative" ref={menuRef}>
                <button
                  onClick={() => setShowUserMenu(!showUserMenu)}
                  className="flex items-center gap-1.5 p-2 hover:bg-white/10 rounded-lg transition-colors"
                  id="user-menu-button"
                >
                  <div className="w-7 h-7 bg-white/20 rounded-full flex items-center justify-center">
                    <User className="w-4 h-4" />
                  </div>
                  <span className="hidden md:block text-sm font-medium max-w-[100px] truncate">
                    {user?.firstName || user?.username || 'User'}
                  </span>
                  <ChevronDown className="w-3.5 h-3.5 hidden md:block" />
                </button>

                {showUserMenu && (
                  <div className="absolute right-0 mt-2 w-56 bg-white rounded-xl shadow-xl py-2 text-gray-700 animate-slide-up z-50">
                    <div className="px-4 py-2 border-b border-gray-100">
                      <p className="text-sm font-semibold text-gray-800">{user?.firstName} {user?.lastName}</p>
                      <p className="text-xs text-gray-400">{user?.username}</p>
                    </div>
                    <Link to="/profile" onClick={() => setShowUserMenu(false)} className="flex items-center gap-2 px-4 py-2.5 hover:bg-gray-50 text-sm">
                      <User className="w-4 h-4" /> Tài khoản
                    </Link>
                    <Link to="/orders" onClick={() => setShowUserMenu(false)} className="flex items-center gap-2 px-4 py-2.5 hover:bg-gray-50 text-sm">
                      <Package className="w-4 h-4" /> Đơn hàng
                    </Link>
                    {user?.role === 'ADMIN' && (
                      <Link to="/admin" onClick={() => setShowUserMenu(false)} className="flex items-center gap-2 px-4 py-2.5 hover:bg-gray-50 text-sm text-primary-500">
                        <LayoutDashboard className="w-4 h-4" /> Quản trị
                      </Link>
                    )}
                    <hr className="my-1 border-gray-100" />
                    <button onClick={handleLogout} className="flex items-center gap-2 px-4 py-2.5 hover:bg-gray-50 text-sm text-red-500 w-full">
                      <LogOut className="w-4 h-4" /> Đăng xuất
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <div className="flex items-center gap-2">
                <Link to="/login" className="text-sm font-medium hover:bg-white/10 px-3 py-1.5 rounded-lg transition-colors">
                  Đăng nhập
                </Link>
                <Link to="/register" className="text-sm font-medium bg-white text-primary-500 px-3 py-1.5 rounded-lg hover:bg-gray-100 transition-colors hidden sm:block">
                  Đăng ký
                </Link>
              </div>
            )}

            {/* Mobile menu toggle */}
            <button onClick={() => setShowMobileMenu(!showMobileMenu)} className="sm:hidden p-2 hover:bg-white/10 rounded-lg">
              {showMobileMenu ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
            </button>
          </div>
        </div>

        {/* Mobile Search */}
        {showMobileMenu && (
          <div className="sm:hidden pb-3 animate-slide-up">
            <form onSubmit={handleSearch}>
              <div className="relative">
                <input
                  type="text"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  placeholder="Tìm kiếm sản phẩm..."
                  className="w-full py-2 pl-4 pr-12 rounded-lg text-gray-800 text-sm focus:outline-none"
                />
                <button type="submit" className="absolute right-0 top-0 h-full px-4 bg-primary-600 rounded-r-lg">
                  <Search className="w-4 h-4" />
                </button>
              </div>
            </form>
          </div>
        )}
      </div>
    </header>
  );
}
