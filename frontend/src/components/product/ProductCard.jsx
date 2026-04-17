import { Link } from 'react-router-dom';
import { Star } from 'lucide-react';
import { formatCurrency } from '../../utils/helpers';

export default function ProductCard({ product }) {
  const minPrice = product.minPrice || product.productDetail?.[0]?.price || 0;

  return (
    <Link to={`/product/${product.id}`} className="card group cursor-pointer">
      <div className="relative overflow-hidden">
        <img
          src={product.imageUrl || 'https://via.placeholder.com/300x300?text=No+Image'}
          alt={product.name}
          className="w-full h-48 sm:h-56 object-cover group-hover:scale-105 transition-transform duration-500"
          loading="lazy"
        />
        {product.productDetail && product.productDetail.length > 1 && (
          <span className="absolute top-2 left-2 bg-primary-500 text-white text-xs px-2 py-0.5 rounded-full">
            {product.productDetail.length} phân loại
          </span>
        )}
      </div>
      <div className="p-3 sm:p-4">
        <h3 className="text-sm font-medium text-gray-800 line-clamp-2 min-h-[2.5rem] group-hover:text-primary-500 transition-colors">
          {product.name}
        </h3>
        <div className="flex items-center gap-1 mt-1.5">
          {product.averageRate > 0 && (
            <>
              <Star className="w-3.5 h-3.5 fill-yellow-400 text-yellow-400" />
              <span className="text-xs text-gray-500">{product.averageRate?.toFixed(1)}</span>
              {product.numRate > 0 && (
                <span className="text-xs text-gray-400">({product.numRate})</span>
              )}
            </>
          )}
          {product.brand && (
            <span className="text-xs text-gray-400 ml-auto">{product.brand}</span>
          )}
        </div>
        <div className="mt-2">
          <span className="text-primary-500 font-bold text-base">{formatCurrency(minPrice)}</span>
        </div>
      </div>
    </Link>
  );
}
