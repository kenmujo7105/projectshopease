import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { SlidersHorizontal, X } from 'lucide-react';
import productApi from '../api/productApi';
import categoryApi from '../api/categoryApi';
import brandApi from '../api/brandApi';
import ProductGrid from '../components/product/ProductGrid';

export default function ProductList() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [brands, setBrands] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [showFilter, setShowFilter] = useState(false);
  const [page, setPage] = useState(0);
  const keyword = searchParams.get('keyword') || '';
  const selectedCategory = searchParams.get('category') || '';

  useEffect(() => {
    const fetchFilters = async () => {
      try {
        const [catRes, brandRes] = await Promise.allSettled([categoryApi.getAll(), brandApi.getAll()]);
        if (catRes.status === 'fulfilled') setCategories(catRes.value.data || []);
        if (brandRes.status === 'fulfilled') setBrands(brandRes.value.data || []);
      } catch {/* skip */}
    };
    fetchFilters();
  }, []);

  useEffect(() => {
    const fetchProducts = async () => {
      setIsLoading(true);
      try {
        let res;
        if (keyword) {
          res = await productApi.search(keyword);
        } else {
          res = await productApi.getAll({ page, size: 20 });
        }
        let data = res.data || [];
        if (selectedCategory) {
          data = data.filter(p => p.category?.includes(selectedCategory));
        }
        setProducts(data);
      } catch {
        setProducts([]);
      } finally {
        setIsLoading(false);
      }
    };
    fetchProducts();
  }, [keyword, page, selectedCategory]);

  const handleCategoryClick = (catName) => {
    const params = new URLSearchParams(searchParams);
    if (catName === selectedCategory) {
      params.delete('category');
    } else {
      params.set('category', catName);
    }
    setSearchParams(params);
  };

  const clearFilters = () => {
    setSearchParams({});
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-6 animate-fade-in">
      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">
            {keyword ? `Kết quả tìm kiếm: "${keyword}"` : 'Tất cả sản phẩm'}
          </h1>
          {(keyword || selectedCategory) && (
            <button onClick={clearFilters} className="text-sm text-primary-500 hover:underline flex items-center gap-1 mt-1">
              <X className="w-3.5 h-3.5" /> Xóa bộ lọc
            </button>
          )}
        </div>
        <button
          onClick={() => setShowFilter(!showFilter)}
          className="lg:hidden btn-ghost flex items-center gap-1.5"
        >
          <SlidersHorizontal className="w-4 h-4" /> Lọc
        </button>
      </div>

      <div className="flex gap-6">
        {/* Sidebar Filters */}
        <aside className={`w-60 flex-shrink-0 ${showFilter ? 'block' : 'hidden'} lg:block`}>
          <div className="bg-white rounded-xl p-5 shadow-sm sticky top-20">
            {/* Categories */}
            <div className="mb-6">
              <h3 className="text-sm font-bold text-gray-700 uppercase tracking-wide mb-3">Danh mục</h3>
              <div className="space-y-1.5">
                {categories.map((cat) => (
                  <button
                    key={cat.id}
                    onClick={() => handleCategoryClick(cat.name)}
                    className={`block w-full text-left text-sm px-3 py-2 rounded-lg transition-colors ${
                      selectedCategory === cat.name
                        ? 'bg-primary-50 text-primary-600 font-medium'
                        : 'text-gray-600 hover:bg-gray-50'
                    }`}
                  >
                    {cat.name}
                  </button>
                ))}
              </div>
            </div>

            {/* Brands */}
            {brands.length > 0 && (
              <div>
                <h3 className="text-sm font-bold text-gray-700 uppercase tracking-wide mb-3">Thương hiệu</h3>
                <div className="space-y-1.5 max-h-48 overflow-y-auto">
                  {brands.map((brand) => (
                    <div key={brand.id} className="text-sm text-gray-600 px-3 py-1.5">
                      {brand.name}
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        </aside>

        {/* Product Grid */}
        <div className="flex-1">
          <ProductGrid products={products} isLoading={isLoading} columns={4} />
          {/* Pagination */}
          {products.length >= 20 && (
            <div className="flex justify-center gap-2 mt-8">
              <button
                onClick={() => setPage(Math.max(0, page - 1))}
                disabled={page === 0}
                className="px-4 py-2 rounded-lg border text-sm disabled:opacity-40 hover:bg-gray-50"
              >
                Trước
              </button>
              <span className="px-4 py-2 bg-primary-500 text-white rounded-lg text-sm font-medium">
                {page + 1}
              </span>
              <button
                onClick={() => setPage(page + 1)}
                className="px-4 py-2 rounded-lg border text-sm hover:bg-gray-50"
              >
                Sau
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
