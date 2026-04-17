import { useState, useEffect } from 'react';
import { Plus, Pencil, Trash2, X, Save } from 'lucide-react';
import toast from 'react-hot-toast';
import brandApi from '../../api/brandApi';

export default function BrandManager() {
  const [brands, setBrands] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({ name: '', description: '', logoUrl: '' });

  const fetchBrands = async () => { try { const res = await brandApi.getAll(); setBrands(res.data || []); } catch {/* skip */} };
  useEffect(() => { fetchBrands(); }, []);

  const resetForm = () => { setForm({ name: '', description: '', logoUrl: '' }); setEditingId(null); setShowForm(false); };

  const handleSubmit = async () => {
    if (!form.name) { toast.error('Tên không được trống'); return; }
    try {
      if (editingId) { await brandApi.update(editingId, form); toast.success('Cập nhật thành công'); }
      else { await brandApi.create(form); toast.success('Thêm thành công'); }
      resetForm(); fetchBrands();
    } catch { toast.error('Lỗi'); }
  };

  const handleEdit = (b) => { setForm({ name: b.name, description: b.description || '', logoUrl: b.logoUrl || '' }); setEditingId(b.id); setShowForm(true); };
  const handleDelete = async (id) => { if (!confirm('Xác nhận xóa?')) return; try { await brandApi.delete(id); toast.success('Đã xóa'); fetchBrands(); } catch { toast.error('Lỗi'); } };

  return (
    <div className="animate-fade-in">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Quản lý thương hiệu</h1>
        <button onClick={() => { resetForm(); setShowForm(true); }} className="btn-primary flex items-center gap-2 text-sm"><Plus className="w-4 h-4" /> Thêm thương hiệu</button>
      </div>

      {showForm && (
        <div className="bg-white rounded-xl shadow-sm p-6 mb-6 animate-slide-up">
          <div className="flex items-center justify-between mb-4"><h2 className="font-bold text-gray-800">{editingId ? 'Chỉnh sửa' : 'Thêm'} thương hiệu</h2><button onClick={resetForm}><X className="w-5 h-5 text-gray-400" /></button></div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <input value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} placeholder="Tên *" className="input-field" />
            <input value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} placeholder="Mô tả" className="input-field" />
            <input value={form.logoUrl} onChange={e => setForm({ ...form, logoUrl: e.target.value })} placeholder="URL logo" className="input-field" />
          </div>
          <button onClick={handleSubmit} className="btn-primary mt-4 flex items-center gap-2 text-sm"><Save className="w-4 h-4" /> Lưu</button>
        </div>
      )}

      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-50"><tr><th className="text-left px-4 py-3">Logo</th><th className="text-left px-4 py-3">Tên</th><th className="text-left px-4 py-3">Mô tả</th><th className="text-right px-4 py-3">Thao tác</th></tr></thead>
          <tbody className="divide-y divide-gray-100">
            {brands.map(b => (
              <tr key={b.id} className="hover:bg-gray-50">
                <td className="px-4 py-3"><div className="w-10 h-10 bg-gray-100 rounded-lg overflow-hidden">{b.logoUrl && <img src={b.logoUrl} alt="" className="w-full h-full object-cover" />}</div></td>
                <td className="px-4 py-3 font-medium text-gray-800">{b.name}</td>
                <td className="px-4 py-3 text-gray-500">{b.description || '—'}</td>
                <td className="px-4 py-3"><div className="flex justify-end gap-1">
                  <button onClick={() => handleEdit(b)} className="p-1.5 text-gray-400 hover:text-blue-500 rounded hover:bg-gray-100"><Pencil className="w-4 h-4" /></button>
                  <button onClick={() => handleDelete(b.id)} className="p-1.5 text-gray-400 hover:text-red-500 rounded hover:bg-gray-100"><Trash2 className="w-4 h-4" /></button>
                </div></td>
              </tr>
            ))}
          </tbody>
        </table>
        {brands.length === 0 && <p className="text-center text-gray-400 py-8">Chưa có thương hiệu nào</p>}
      </div>
    </div>
  );
}
