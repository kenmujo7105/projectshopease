import { useState, useEffect } from 'react';
import { Trash2 } from 'lucide-react';
import toast from 'react-hot-toast';
import userApi from '../../api/userApi';

export default function UserManager() {
  const [users, setUsers] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  const fetchUsers = async () => { setIsLoading(true); try { const res = await userApi.getAll(); setUsers(res.data || []); } catch {/* skip */} finally { setIsLoading(false); } };
  useEffect(() => { fetchUsers(); }, []);

  const handleDelete = async (id) => {
    if (!confirm('Xác nhận xóa người dùng?')) return;
    try { await userApi.delete(id); toast.success('Đã xóa'); fetchUsers(); } catch { toast.error('Lỗi'); }
  };

  return (
    <div className="animate-fade-in">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Quản lý người dùng</h1>
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-50">
            <tr>
              <th className="text-left px-4 py-3">Tên</th>
              <th className="text-left px-4 py-3">Email</th>
              <th className="text-left px-4 py-3">Vai trò</th>
              <th className="text-right px-4 py-3">Thao tác</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {users.map(u => (
              <tr key={u.id} className="hover:bg-gray-50">
                <td className="px-4 py-3">
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 bg-primary-100 rounded-full flex items-center justify-center text-primary-600 text-xs font-bold">{u.firstName?.[0] || 'U'}</div>
                    <span className="font-medium text-gray-800">{u.firstName} {u.lastName}</span>
                  </div>
                </td>
                <td className="px-4 py-3 text-gray-500">{u.username}</td>
                <td className="px-4 py-3"><span className={`badge ${u.role === 'ADMIN' ? 'bg-purple-100 text-purple-700' : 'bg-gray-100 text-gray-600'}`}>{u.role}</span></td>
                <td className="px-4 py-3"><div className="flex justify-end">
                  <button onClick={() => handleDelete(u.id)} className="p-1.5 text-gray-400 hover:text-red-500 rounded hover:bg-gray-100"><Trash2 className="w-4 h-4" /></button>
                </div></td>
              </tr>
            ))}
          </tbody>
        </table>
        {users.length === 0 && !isLoading && <p className="text-center text-gray-400 py-8">Chưa có người dùng nào</p>}
      </div>
    </div>
  );
}
