import { PackageOpen } from 'lucide-react';

export default function EmptyState({ title = 'Không có dữ liệu', description = '', icon: Icon = PackageOpen }) {
  return (
    <div className="flex flex-col items-center justify-center py-16 text-gray-400">
      <Icon className="w-16 h-16 mb-4" strokeWidth={1} />
      <h3 className="text-lg font-semibold text-gray-500">{title}</h3>
      {description && <p className="text-sm mt-1">{description}</p>}
    </div>
  );
}
