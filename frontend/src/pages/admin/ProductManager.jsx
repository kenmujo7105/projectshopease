import { useState, useEffect } from 'react';
import { Plus, Pencil, Trash2, X, Save } from 'lucide-react';
import toast from 'react-hot-toast';
import productApi from '../../api/productApi';
import categoryApi from '../../api/categoryApi';
import brandApi from '../../api/brandApi';
import { formatCurrency } from '../../utils/helpers';

export default function ProductManager() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [brands, setBrands] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({ name: '', description: '', imageUrl: '', brand: '', productCategory: [], productDetails: [{ info: '', price: 0, quantity: 0 }] });
  const [isLoading, setIsLoading] = useState(true);

  const fetchAll = async () => {
    setIsLoading(true);
    try {
      const [p, c, b] = await Promise.allSettled([productApi.getAll({ page: 0, size: 100 }), categoryApi.getAll(), brandApi.getAll()]);
      if (p.status === 'fulfilled') setProducts(p.value.data || []);
      if (c.status === 'fulfilled') setCategories(c.value.data || []);
      if (b.status === 'fulfilled') setBrands(b.value.data || []);
    } catch {/* skip */} finally { setIsLoading(false); }
  };

  useEffect(() => { fetchAll(); }, []);

  const resetForm = () => { setForm({ name: '', description: '', imageUrl: '', brand: '', productCategory: [], productDetails: [{ info: '', price: 0, quantity: 0 }] }); setEditingId(null); setShowForm(false); };

  const handleSubmit = async () => {
    if (!form.name) { toast.error('Tên sản phẩm không được trống'); return; }
    try {
      if (editingId) {
        await productApi.updateInfo(editingId, { name: form.name, description: form.description, imageUrl: form.imageUrl, brand: form.brand, productCategory: form.productCategory });
      } else {
        await productApi.create({ name: form.name, description: form.description, imageUrl: form.imageUrl, brand: form.brand, productDetails: form.productDetails, productCategory: form.productCategory });
      }
      toast.success(editingId ? 'Cập nhật thành công' : 'Tạo sản phẩm thành công');
      resetForm(); fetchAll();
    } catch { toast.error('Lỗi xử lý sản phẩm'); }
  };

  const handleEdit = (p) => {
    setForm({ name: p.name, description: p.description || '', imageUrl: p.imageUrl || '', brand: p.brand || '', productCategory: p.category || [], productDetails: p.productDetail || [] });
    setEditingId(p.id); setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!confirm('Xác nhận xóa sản phẩm?')) return;
    try { await productApi.delete(id); toast.success('Đã xóa'); fetchAll(); } catch { toast.error('Lỗi xóa'); }
  };

  const addDetail = () => setForm({ ...form, productDetails: [...form.productDetails, { info: '', price: 0, quantity: 0 }] });
  const updateDetail = (idx, field, value) => { const d = [...form.productDetails]; d[idx] = { ...d[idx], [field]: value }; setForm({ ...form, productDetails: d }); };
  const removeDetail = (idx) => setForm({ ...form, productDetails: form.productDetails.filter((_, i) => i !== idx) });

  return (
    <div className="animate-fade-in">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Quản lý sản phẩm</h1>
        <button onClick={() => { resetForm(); setShowForm(true); }} className="btn-primary flex items-center gap-2 text-sm"><Plus className="w-4 h-4" /> Thêm sản phẩm</button>
      </div>

      {showForm && (
        <div className="bg-white rounded-xl shadow-sm p-6 mb-6 animate-slide-up">
          <div className="flex items-center justify-between mb-4">
            <h2 className="font-bold text-gray-800">{editingId ? 'Chỉnh sửa' : 'Thêm mới'} sản phẩm</h2>
            <button onClick={resetForm} className="text-gray-400 hover:text-gray-600"><X className="w-5 h-5" /></button>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <div><label className="block text-sm text-gray-600 mb-1">Tên sản phẩm *</label><input value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} className="input-field" placeholder="Nhập tên sản phẩm" /></div>
            <div><label className="block text-sm text-gray-600 mb-1">URL hình ảnh</label><input value={form.imageUrl} onChange={e => setForm({ ...form, imageUrl: e.target.value })} className="input-field" placeholder="https://..." /></div>
            <div><label className="block text-sm text-gray-600 mb-1">Thương hiệu</label>
              <select value={form.brand} onChange={e => setForm({ ...form, brand: e.target.value })} className="input-field">
                <option value="">Chọn thương hiệu</option>
                {brands.map(b => <option key={b.id} value={b.name}>{b.name}</option>)}
              </select>
            </div>
            <div><label className="block text-sm text-gray-600 mb-1">Danh mục</label>
              <select multiple value={form.productCategory} onChange={e => setForm({ ...form, productCategory: Array.from(e.target.selectedOptions, o => o.value) })} className="input-field h-20">
                {categories.map(c => <option key={c.id} value={c.name}>{c.name}</option>)}
              </select>
            </div>
          </div>
          <div><label className="block text-sm text-gray-600 mb-1">Mô tả</label><textarea value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} className="input-field h-20 resize-none" /></div>

          {!editingId && (
            <div className="mt-4">
              <div className="flex items-center justify-between mb-2"><h3 className="text-sm font-semibold text-gray-700">Phân loại sản phẩm</h3><button onClick={addDetail} className="text-primary-500 text-sm hover:underline flex items-center gap-1"><Plus className="w-3.5 h-3.5" /> Thêm</button></div>
              {form.productDetails.map((d, idx) => (
                <div key={idx} className="grid grid-cols-4 gap-3 mb-2">
                  <input value={d.info} onChange={e => updateDetail(idx, 'info', e.target.value)} placeholder="Tên (VD: Đỏ - XL)" className="input-field text-sm" />
                  <input type="number" value={d.price} onChange={e => updateDetail(idx, 'price', Number(e.target.value))} placeholder="Giá" className="input-field text-sm" />
                  <input type="number" value={d.quantity} onChange={e => updateDetail(idx, 'quantity', Number(e.target.value))} placeholder="Số lượng" className="input-field text-sm" />
                  <button onClick={() => removeDetail(idx)} className="text-red-400 hover:text-red-600 justify-self-start"><Trash2 className="w-4 h-4" /></button>
                </div>
              ))}
            </div>
          )}

          <button onClick={handleSubmit} className="btn-primary mt-4 flex items-center gap-2"><Save className="w-4 h-4" /> {editingId ? 'Cập nhật' : 'Tạo sản phẩm'}</button>
        </div>
      )}

      {/* Product Table */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead className="bg-gray-50 text-gray-600">
              <tr>
                <th className="text-left px-4 py-3 font-semibold">Sản phẩm</th>
                <th className="text-left px-4 py-3 font-semibold">Danh mục</th>
                <th className="text-left px-4 py-3 font-semibold">Giá</th>
                <th className="text-left px-4 py-3 font-semibold">Phân loại</th>
                <th className="text-right px-4 py-3 font-semibold">Thao tác</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {products.map(p => (
                <tr key={p.id} className="hover:bg-gray-50 transition-colors">
                  <td className="px-4 py-3">
                    <div className="flex items-center gap-3">
                      <img src={p.imageUrl || 'https://via.placeholder.com/40'} alt="" className="w-10 h-10 rounded-lg object-cover" />
                      <div><p className="font-medium text-gray-800 line-clamp-1">{p.name}</p><p className="text-xs text-gray-400">{p.brand}</p></div>
                    </div>
                  </td>
                  <td className="px-4 py-3 text-gray-500">{p.category?.join(', ') || '—'}</td>
                  <td className="px-4 py-3 text-primary-500 font-medium">{formatCurrency(p.minPrice)}</td>
                  <td className="px-4 py-3 text-gray-500">{p.productDetail?.length || 0}</td>
                  <td className="px-4 py-3">
                    <div className="flex justify-end gap-1">
                      <button onClick={() => handleEdit(p)} className="p-1.5 text-gray-400 hover:text-blue-500 rounded hover:bg-gray-100"><Pencil className="w-4 h-4" /></button>
                      <button onClick={() => handleDelete(p.id)} className="p-1.5 text-gray-400 hover:text-red-500 rounded hover:bg-gray-100"><Trash2 className="w-4 h-4" /></button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {products.length === 0 && !isLoading && <p className="text-center text-gray-400 py-8">Chưa có sản phẩm nào</p>}
      </div>
    </div>
  );
}
