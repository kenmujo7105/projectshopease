import ProductCard from './ProductCard';
import SkeletonCard from '../common/SkeletonCard';
import EmptyState from '../common/EmptyState';

export default function ProductGrid({ products, isLoading, columns = 5 }) {
  const colClass = {
    2: 'grid-cols-1 sm:grid-cols-2',
    3: 'grid-cols-1 sm:grid-cols-2 lg:grid-cols-3',
    4: 'grid-cols-2 sm:grid-cols-3 lg:grid-cols-4',
    5: 'grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5',
    6: 'grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6',
  };

  if (isLoading) {
    return (
      <div className={`grid ${colClass[columns]} gap-3 sm:gap-4`}>
        {Array.from({ length: columns * 2 }).map((_, i) => (
          <SkeletonCard key={i} />
        ))}
      </div>
    );
  }

  if (!products || products.length === 0) {
    return <EmptyState title="Không tìm thấy sản phẩm" description="Thử tìm kiếm với từ khóa khác" />;
  }

  return (
    <div className={`grid ${colClass[columns]} gap-3 sm:gap-4`}>
      {products.map((product) => (
        <ProductCard key={product.id} product={product} />
      ))}
    </div>
  );
}
