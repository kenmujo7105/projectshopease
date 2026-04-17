import { Link } from 'react-router-dom';

export default function Footer() {
  return (
    <footer className="bg-dark-900 text-gray-300 mt-auto">
      <div className="max-w-7xl mx-auto px-4 py-12">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
          {/* Brand */}
          <div>
            <div className="flex items-center gap-2 mb-4">
              <div className="w-8 h-8 bg-primary-500 rounded-lg flex items-center justify-center">
                <span className="text-white font-extrabold text-lg">S</span>
              </div>
              <span className="text-xl font-bold text-white">ShopEase</span>
            </div>
            <p className="text-sm text-gray-400 leading-relaxed">
              Nền tảng mua sắm trực tuyến hàng đầu với hàng ngàn sản phẩm chất lượng cao và giá cả phải chăng.
            </p>
          </div>

          {/* Customer Service */}
          <div>
            <h4 className="text-white font-semibold mb-4">Chăm sóc khách hàng</h4>
            <ul className="space-y-2 text-sm">
              <li><Link to="/products" className="hover:text-primary-400 transition-colors">Sản phẩm</Link></li>
              <li><Link to="/cart" className="hover:text-primary-400 transition-colors">Giỏ hàng</Link></li>
              <li><Link to="/orders" className="hover:text-primary-400 transition-colors">Lịch sử đơn hàng</Link></li>
            </ul>
          </div>

          {/* About */}
          <div>
            <h4 className="text-white font-semibold mb-4">Về ShopEase</h4>
            <ul className="space-y-2 text-sm">
              <li><a href="#" className="hover:text-primary-400 transition-colors">Giới thiệu</a></li>
              <li><a href="#" className="hover:text-primary-400 transition-colors">Điều khoản sử dụng</a></li>
              <li><a href="#" className="hover:text-primary-400 transition-colors">Chính sách bảo mật</a></li>
            </ul>
          </div>

          {/* Contact */}
          <div>
            <h4 className="text-white font-semibold mb-4">Liên hệ</h4>
            <ul className="space-y-2 text-sm">
              <li className="text-gray-400">Email: support@shopease.vn</li>
              <li className="text-gray-400">Hotline: 1900 xxxx</li>
              <li className="text-gray-400">Địa chỉ: TP. Hồ Chí Minh, Việt Nam</li>
            </ul>
          </div>
        </div>

        <div className="border-t border-gray-800 mt-8 pt-6 text-center text-sm text-gray-500">
          &copy; {new Date().getFullYear()} ShopEase. All rights reserved.
        </div>
      </div>
    </footer>
  );
}
