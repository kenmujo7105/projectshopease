import { useState, useEffect } from 'react';
import { Plus, Pencil, Trash2, X, Save } from 'lucide-react';
import toast from 'react-hot-toast';
import categoryApi from '../../api/categoryApi';

export default function CategoryManager() {
  const [categories, setCategories] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({ name: '', description: '', urlImage: '' });

  const fetchCategories = async () => { try { const res = await categoryApi.getAll(); setCategories(res.data || []); } catch {/* skip */} };
  useEffect(() => { fetchCategories(); }, []);

  const resetForm = () => { setForm({ name: '', description: '', urlImage: '' }); setEditingId(null); setShowForm(false); };

  const handleSubmit = async () => {
    if (!form.name) { toast.error('Tên không được trống'); return; }
    try {
      if (editingId) { await categoryApi.update(editingId, form); toast.success('Cập nhật thành công'); }
      else { await categoryApi.create(form); toast.success('Thêm thành công'); }
      resetForm(); fetchCategories();
    } catch { toast.error('Lỗi'); }
  };

  const handleEdit = (c) => { setForm({ name: c.name, description: c.description || '', urlImage: c.urlImage || '' }); setEditingId(c.id); setShowForm(true); };
  const handleDelete = async (id) => { if (!confirm('Xác nhận xóa?')) return; try { await categoryApi.delete(id); toast.success('Đã xóa'); fetchCategories(); } catch { toast.error('Lỗi'); } };

  return (
    <div className="animate-fade-in">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Quản lý danh mục</h1>
        <button onClick={() => { resetForm(); setShowForm(true); }} className="btn-primary flex items-center gap-2 text-sm"><Plus className="w-4 h-4" /> Thêm danh mục</button>
      </div>

      {showForm && (
        <div className="bg-white rounded-xl shadow-sm p-6 mb-6 animate-slide-up">
          <div className="flex items-center justify-between mb-4"><h2 className="font-bold text-gray-800">{editingId ? 'Chỉnh sửa' : 'Thêm'} danh mục</h2><button onClick={resetForm}><X className="w-5 h-5 text-gray-400" /></button></div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <input value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} placeholder="Tên danh mục *" className="input-field" />
            <input value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} placeholder="Mô tả" className="input-field" />
            <input value={form.urlImage} onChange={e => setForm({ ...form, urlImage: e.target.value })} placeholder="URL hình ảnh" className="input-field" />
          </div>
          <button onClick={handleSubmit} className="btn-primary mt-4 flex items-center gap-2 text-sm"><Save className="w-4 h-4" /> Lưu</button>
        </div>
      )}

      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-50"><tr><th className="text-left px-4 py-3">Hình</th><th className="text-left px-4 py-3">Tên</th><th className="text-left px-4 py-3">Mô tả</th><th className="text-right px-4 py-3">Thao tác</th></tr></thead>
          <tbody className="divide-y divide-gray-100">
            {categories.map(c => (
              <tr key={c.id} className="hover:bg-gray-50">
                <td className="px-4 py-3"><div className="w-10 h-10 bg-gray-100 rounded-lg overflow-hidden">{c.urlImage && <img src={c.urlImage} alt="" className="w-full h-full object-cover" />}</div></td>
                <td className="px-4 py-3 font-medium text-gray-800">{c.name}</td>
                <td className="px-4 py-3 text-gray-500">{c.description || '—'}</td>
                <td className="px-4 py-3"><div className="flex justify-end gap-1">
                  <button onClick={() => handleEdit(c)} className="p-1.5 text-gray-400 hover:text-blue-500 rounded hover:bg-gray-100"><Pencil className="w-4 h-4" /></button>
                  <button onClick={() => handleDelete(c.id)} className="p-1.5 text-gray-400 hover:text-red-500 rounded hover:bg-gray-100"><Trash2 className="w-4 h-4" /></button>
                </div></td>
              </tr>
            ))}
          </tbody>
        </table>
        {categories.length === 0 && <p className="text-center text-gray-400 py-8">Chưa có danh mục nào</p>}
      </div>
    </div>
  );
}
