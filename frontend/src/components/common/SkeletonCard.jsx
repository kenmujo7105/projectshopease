export default function SkeletonCard() {
  return (
    <div className="card p-0 animate-fade-in">
      <div className="skeleton h-48 w-full" />
      <div className="p-4 space-y-3">
        <div className="skeleton h-4 w-3/4" />
        <div className="skeleton h-4 w-1/2" />
        <div className="skeleton h-6 w-1/3" />
      </div>
    </div>
  );
}
