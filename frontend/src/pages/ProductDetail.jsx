import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { ShoppingCart, Minus, Plus, Star, Package } from 'lucide-react';
import toast from 'react-hot-toast';
import productApi from '../api/productApi';
import feedbackApi from '../api/feedbackApi';
import useCartStore from '../store/useCartStore';
import useAuthStore from '../store/useAuthStore';
import StarRating from '../components/common/StarRating';
import LoadingSpinner from '../components/common/LoadingSpinner';
import { formatCurrency } from '../utils/helpers';

export default function ProductDetail() {
  const { id } = useParams();
  const { addItem } = useCartStore();
  const { isAuthenticated } = useAuthStore();
  const [product, setProduct] = useState(null);
  const [feedbacks, setFeedbacks] = useState([]);
  const [selectedDetail, setSelectedDetail] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [isLoading, setIsLoading] = useState(true);
  const [addingToCart, setAddingToCart] = useState(false);

  useEffect(() => {
    const fetchProduct = async () => {
      setIsLoading(true);
      try {
        const res = await productApi.getById(id);
        const p = res.data;
        setProduct(p);
        if (p.productDetail?.length > 0) setSelectedDetail(p.productDetail[0]);
      } catch {
        toast.error('Không tìm thấy sản phẩm');
      } finally {
        setIsLoading(false);
      }
    };
    fetchProduct();
  }, [id]);

  useEffect(() => {
    const fetchFeedback = async () => {
      try {
        const res = await feedbackApi.getByProduct(id, { page: 0, size: 10 });
        setFeedbacks(res.data || []);
      } catch {/* skip */}
    };
    if (id) fetchFeedback();
  }, [id]);

  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      toast.error('Vui lòng đăng nhập để thêm vào giỏ hàng');
      return;
    }
    if (!selectedDetail) {
      toast.error('Vui lòng chọn phân loại sản phẩm');
      return;
    }
    setAddingToCart(true);
    try {
      await addItem(product.id, selectedDetail.id, quantity);
      toast.success('Đã thêm vào giỏ hàng!');
    } catch {
      toast.error('Lỗi khi thêm vào giỏ hàng');
    } finally {
      setAddingToCart(false);
    }
  };

  if (isLoading) return <LoadingSpinner className="py-32" size="lg" />;
  if (!product) return <div className="text-center py-32 text-gray-400">Sản phẩm không tồn tại</div>;

  return (
    <div className="max-w-7xl mx-auto px-4 py-6 animate-fade-in">
      <div className="bg-white rounded-2xl shadow-sm overflow-hidden">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-0">
          {/* Image */}
          <div className="p-6 flex items-center justify-center bg-gray-50">
            <img
              src={product.imageUrl || 'https://via.placeholder.com/500x500?text=No+Image'}
              alt={product.name}
              className="max-h-96 object-contain rounded-xl"
            />
          </div>

          {/* Info */}
          <div className="p-6 md:p-8 flex flex-col">
            {/* Categories */}
            {product.category?.length > 0 && (
              <div className="flex flex-wrap gap-1.5 mb-3">
                {product.category.map((cat) => (
                  <span key={cat} className="bg-primary-50 text-primary-600 text-xs font-medium px-2.5 py-0.5 rounded-full">
                    {cat}
                  </span>
                ))}
              </div>
            )}

            <h1 className="text-xl sm:text-2xl font-bold text-gray-900 leading-snug">{product.name}</h1>

            {/* Rating */}
            <div className="flex items-center gap-3 mt-3">
              <StarRating rating={product.averageRate || 0} size="md" />
              <span className="text-sm text-gray-500">
                {product.averageRate?.toFixed(1) || '0'} ({product.numRate || 0} đánh giá)
              </span>
              {product.brand && (
                <span className="text-sm text-gray-400 ml-auto">Thương hiệu: <strong className="text-gray-600">{product.brand}</strong></span>
              )}
            </div>

            {/* Price */}
            <div className="mt-6 bg-gray-50 rounded-xl p-4">
              <span className="text-3xl font-extrabold text-primary-500">
                {formatCurrency(selectedDetail?.price || product.minPrice)}
              </span>
            </div>

            {/* Variants */}
            {product.productDetail?.length > 0 && (
              <div className="mt-6">
                <h3 className="text-sm font-semibold text-gray-700 mb-2">Phân loại:</h3>
                <div className="flex flex-wrap gap-2">
                  {product.productDetail.map((detail) => (
                    <button
                      key={detail.id}
                      onClick={() => { setSelectedDetail(detail); setQuantity(1); }}
                      className={`px-4 py-2 rounded-lg border text-sm font-medium transition-all ${
                        selectedDetail?.id === detail.id
                          ? 'border-primary-500 bg-primary-50 text-primary-600'
                          : 'border-gray-200 text-gray-600 hover:border-gray-300'
                      }`}
                    >
                      {detail.info} — {formatCurrency(detail.price)}
                    </button>
                  ))}
                </div>
                {selectedDetail && (
                  <p className="text-xs text-gray-400 mt-2 flex items-center gap-1">
                    <Package className="w-3.5 h-3.5" /> Còn {selectedDetail.quantity} sản phẩm
                  </p>
                )}
              </div>
            )}

            {/* Quantity */}
            <div className="mt-6 flex items-center gap-4">
              <span className="text-sm font-semibold text-gray-700">Số lượng:</span>
              <div className="flex items-center border rounded-lg">
                <button
                  onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  className="p-2 hover:bg-gray-50 transition-colors"
                >
                  <Minus className="w-4 h-4" />
                </button>
                <span className="px-4 py-1.5 text-sm font-medium min-w-[40px] text-center">{quantity}</span>
                <button
                  onClick={() => setQuantity(Math.min(selectedDetail?.quantity || 99, quantity + 1))}
                  className="p-2 hover:bg-gray-50 transition-colors"
                >
                  <Plus className="w-4 h-4" />
                </button>
              </div>
            </div>

            {/* Add to cart */}
            <div className="mt-auto pt-6 flex gap-3">
              <button
                onClick={handleAddToCart}
                disabled={addingToCart || !selectedDetail}
                className="btn-primary flex-1 flex items-center justify-center gap-2 disabled:opacity-50"
              >
                <ShoppingCart className="w-5 h-5" />
                {addingToCart ? 'Đang thêm...' : 'Thêm vào giỏ hàng'}
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Description */}
      {product.description && (
        <div className="bg-white rounded-2xl shadow-sm p-6 mt-6">
          <h2 className="text-lg font-bold text-gray-800 mb-3">Mô tả sản phẩm</h2>
          <p className="text-gray-600 leading-relaxed whitespace-pre-wrap">{product.description}</p>
        </div>
      )}

      {/* Reviews */}
      <div className="bg-white rounded-2xl shadow-sm p-6 mt-6">
        <h2 className="text-lg font-bold text-gray-800 mb-4 flex items-center gap-2">
          <Star className="w-5 h-5 text-yellow-400" /> Đánh giá ({feedbacks.length})
        </h2>
        {feedbacks.length === 0 ? (
          <p className="text-gray-400 text-sm py-4">Chưa có đánh giá nào cho sản phẩm này.</p>
        ) : (
          <div className="space-y-4">
            {feedbacks.map((fb) => (
              <div key={fb.id} className="border-b border-gray-100 pb-4 last:border-0">
                <div className="flex items-center gap-2 mb-1">
                  <div className="w-8 h-8 bg-primary-100 rounded-full flex items-center justify-center text-primary-600 text-xs font-bold">
                    {fb.user?.firstName?.[0] || 'U'}
                  </div>
                  <span className="text-sm font-medium text-gray-700">{fb.user?.firstName} {fb.user?.lastName}</span>
                  <StarRating rating={fb.rate || 0} size="sm" />
                </div>
                <p className="text-sm text-gray-600 ml-10">{fb.comment}</p>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
