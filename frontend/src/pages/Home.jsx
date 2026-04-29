import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight, Truck, Shield, Headphones, CreditCard } from 'lucide-react';
import productApi from '../api/productApi';
import categoryApi from '../api/categoryApi';
import ProductGrid from '../components/product/ProductGrid';

export default function Home() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      try {
        const [prodRes, catRes] = await Promise.allSettled([
          productApi.getAll({ page: 0, size: 10 }),
          categoryApi.getAll(),
        ]);
        if (prodRes.status === 'fulfilled') setProducts(prodRes.value.data || []);
        if (catRes.status === 'fulfilled') setCategories(catRes.value.data || []);
      } catch {
        // fallback handled by empty arrays
      } finally {
        setIsLoading(false);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="animate-fade-in">
      {/* Hero Banner */}
      <section className="bg-gradient-to-br from-primary-500 via-primary-600 to-primary-700 text-white">
        <div className="max-w-7xl mx-auto px-4 py-16 sm:py-24 flex flex-col md:flex-row items-center gap-8">
          <div className="flex-1 text-center md:text-left">
            <h1 className="text-3xl sm:text-5xl font-extrabold leading-tight mb-4">
              Mua sắm thông minh
              <br />
              <span className="text-yellow-300">Giá cả tốt nhất</span>
            </h1>
            <p className="text-white/80 text-lg mb-8 max-w-lg">
              Khám phá hàng ngàn sản phẩm chất lượng với giá ưu đãi. Giao hàng nhanh, đổi trả dễ dàng.
            </p>
            <Link to="/products" className="inline-flex items-center gap-2 bg-white text-primary-600 font-bold py-3 px-8 rounded-xl hover:bg-gray-100 transition-all shadow-lg hover:shadow-xl">
              Khám phá ngay <ArrowRight className="w-5 h-5" />
            </Link>
          </div>
          <div className="flex-1 hidden md:flex justify-center">
            <div className="relative">
              <div className="w-72 h-72 bg-white/10 rounded-full flex items-center justify-center backdrop-blur-sm">
                <div className="w-56 h-56 bg-white/10 rounded-full flex items-center justify-center">
                  <div className="text-center">
                    <p className="text-6xl font-extrabold text-yellow-300">50%</p>
                    <p className="text-lg font-semibold mt-1">GIẢM GIÁ</p>
                  </div>
                </div>
              </div>
              <div className="absolute -top-4 -right-4 bg-yellow-400 text-gray-900 font-bold text-sm px-4 py-2 rounded-full animate-bounce">
                HOT DEAL
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="bg-white border-b">
        <div className="max-w-7xl mx-auto px-4 py-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {[
              { icon: Truck, title: 'Miễn phí vận chuyển', desc: 'Đơn hàng từ 300K' },
              { icon: Shield, title: 'Bảo hành chính hãng', desc: '100% chính hãng' },
              { icon: Headphones, title: 'Hỗ trợ 24/7', desc: 'Tư vấn tận tâm' },
              { icon: CreditCard, title: 'Thanh toán an toàn', desc: 'Bảo mật SSL' },
            ].map(({ icon: Icon, title, desc }) => (
              <div key={title} className="flex items-center gap-3 p-3">
                <div className="w-10 h-10 bg-primary-50 rounded-lg flex items-center justify-center flex-shrink-0">
                  <Icon className="w-5 h-5 text-primary-500" />
                </div>
                <div>
                  <p className="text-sm font-semibold text-gray-800">{title}</p>
                  <p className="text-xs text-gray-400">{desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Categories */}
      {categories.length > 0 && (
        <section className="max-w-7xl mx-auto px-4 py-10">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-bold text-gray-800">Danh mục sản phẩm</h2>
            <Link to="/products" className="text-sm text-primary-500 hover:underline flex items-center gap-1">
              Xem tất cả <ArrowRight className="w-4 h-4" />
            </Link>
          </div>
          <div className="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-6 lg:grid-cols-7 gap-3">
            {categories.map((cat) => (
              <Link
                key={cat.id}
                to={`/products?category=${cat.name}`}
                className="flex flex-col items-center p-4 bg-white rounded-xl hover:shadow-md transition-all hover:-translate-y-0.5 group"
              >
                <div className="w-14 h-14 bg-primary-50 rounded-full flex items-center justify-center mb-2 group-hover:bg-primary-100 transition-colors overflow-hidden">
                  {cat.urlImage ? (
                    <img src={cat.urlImage} alt={cat.name} className="w-full h-full object-cover rounded-full" />
                  ) : (
                    <span className="text-2xl">📦</span>
                  )}
                </div>
                <span className="text-xs text-center text-gray-600 font-medium line-clamp-2">{cat.name}</span>
              </Link>
            ))}
          </div>
        </section>
      )}

      {/* Featured Products */}
      <section className="max-w-7xl mx-auto px-4 pb-12">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-bold text-gray-800 flex items-center gap-2">
            <span className="w-1.5 h-6 bg-primary-500 rounded-full" />
            Sản phẩm nổi bật
          </h2>
          <Link to="/products" className="text-sm text-primary-500 hover:underline flex items-center gap-1">
            Xem thêm <ArrowRight className="w-4 h-4" />
          </Link>
        </div>
        <ProductGrid products={products} isLoading={isLoading} columns={5} />
      </section>
    </div>
  );
}
