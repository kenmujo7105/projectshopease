import { useState, useEffect } from 'react';
import { User, MapPin, Lock, Plus, Trash2, Edit2, Save, X } from 'lucide-react';
import toast from 'react-hot-toast';
import useAuthStore from '../store/useAuthStore';
import userApi from '../api/userApi';
import addressInfoApi from '../api/addressInfoApi';

export default function Profile() {
  const { user, fetchProfile } = useAuthStore();
  const [tab, setTab] = useState('info');
  const [form, setForm] = useState({ firstName: '', lastName: '' });
  const [addresses, setAddresses] = useState([]);
  const [passwordForm, setPasswordForm] = useState({ oldPassword: '', newPassword: '', confirmPassword: '' });
  const [showAddressForm, setShowAddressForm] = useState(false);
  const [addressForm, setAddressForm] = useState({ province: '', provinceId: null, district: '', districtId: null, ward: '', wardId: null, info: '', phone: '', receiverName: '', isDefault: false });
  const [provinces, setProvinces] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [wards, setWards] = useState([]);
  const [isSaving, setIsSaving] = useState(false);

  useEffect(() => {
    if (user) {
      setForm({ firstName: user.firstName || '', lastName: user.lastName || '' });
      loadAddresses();
    }
  }, [user]);

  const loadAddresses = async () => {
    try {
      const res = await userApi.getAddresses(user.id);
      setAddresses(res.data || []);
    } catch {/* skip */}
  };

  useEffect(() => {
    addressInfoApi.getProvinces().then(r => setProvinces(r.data || [])).catch(() => {});
  }, []);

  const handleProvinceChange = async (e) => {
    const selected = provinces.find(p => p.provinceId === Number(e.target.value));
    setAddressForm({ ...addressForm, provinceId: selected?.provinceId, province: selected?.provinceName || '', districtId: null, district: '', wardId: null, ward: '' });
    setDistricts([]); setWards([]);
    if (selected) {
      try { const r = await addressInfoApi.getDistricts(selected.provinceId); setDistricts(r.data || []); } catch {/* skip */}
    }
  };

  const handleDistrictChange = async (e) => {
    const selected = districts.find(d => d.districtId === Number(e.target.value));
    setAddressForm({ ...addressForm, districtId: selected?.districtId, district: selected?.districtName || '', wardId: null, ward: '' });
    setWards([]);
    if (selected) {
      try { const r = await addressInfoApi.getWards(selected.districtId); setWards(r.data || []); } catch {/* skip */}
    }
  };

  const handleWardChange = (e) => {
    const selected = wards.find(w => w.wardCode === e.target.value);
    setAddressForm({ ...addressForm, wardId: Number(selected?.wardCode), ward: selected?.wardName || '' });
  };

  const handleUpdateProfile = async () => {
    setIsSaving(true);
    try {
      await userApi.update(user.id, form);
      await fetchProfile();
      toast.success('Cập nhật thành công');
    } catch { toast.error('Lỗi cập nhật'); } finally { setIsSaving(false); }
  };

  const handleChangePassword = async () => {
    if (passwordForm.newPassword !== passwordForm.confirmPassword) { toast.error('Mật khẩu xác nhận không khớp'); return; }
    setIsSaving(true);
    try {
      await userApi.updatePassword(user.id, { oldPassword: passwordForm.oldPassword, newPassword: passwordForm.newPassword });
      toast.success('Đổi mật khẩu thành công');
      setPasswordForm({ oldPassword: '', newPassword: '', confirmPassword: '' });
    } catch { toast.error('Mật khẩu cũ không đúng'); } finally { setIsSaving(false); }
  };

  const handleSaveAddress = async () => {
    setIsSaving(true);
    try {
      await userApi.createAddress(user.id, addressForm);
      await loadAddresses();
      setShowAddressForm(false);
      setAddressForm({ province: '', provinceId: null, district: '', districtId: null, ward: '', wardId: null, info: '', phone: '', receiverName: '', isDefault: false });
      toast.success('Thêm địa chỉ thành công');
    } catch { toast.error('Lỗi thêm địa chỉ'); } finally { setIsSaving(false); }
  };

  const handleDeleteAddress = async (addrId) => {
    try { await userApi.deleteAddress(user.id, addrId); await loadAddresses(); toast.success('Đã xóa'); } catch { toast.error('Lỗi'); }
  };

  const tabs = [
    { key: 'info', label: 'Thông tin', icon: User },
    { key: 'address', label: 'Địa chỉ', icon: MapPin },
    { key: 'password', label: 'Mật khẩu', icon: Lock },
  ];

  return (
    <div className="max-w-4xl mx-auto px-4 py-8 animate-fade-in">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Tài khoản của tôi</h1>
      <div className="flex flex-col md:flex-row gap-6">
        {/* Sidebar */}
        <div className="w-full md:w-56 flex-shrink-0">
          <div className="bg-white rounded-xl shadow-sm p-4 space-y-1">
            {tabs.map(t => (
              <button key={t.key} onClick={() => setTab(t.key)} className={`w-full flex items-center gap-2 px-4 py-2.5 rounded-lg text-sm font-medium transition-colors ${tab === t.key ? 'bg-primary-50 text-primary-600' : 'text-gray-500 hover:bg-gray-50'}`}>
                <t.icon className="w-4 h-4" /> {t.label}
              </button>
            ))}
          </div>
        </div>

        {/* Content */}
        <div className="flex-1 bg-white rounded-xl shadow-sm p-6">
          {tab === 'info' && (
            <div className="space-y-4">
              <h2 className="font-bold text-gray-800 mb-4">Thông tin cá nhân</h2>
              <div className="grid grid-cols-2 gap-4">
                <div><label className="block text-sm text-gray-600 mb-1">Họ</label><input value={form.firstName} onChange={e => setForm({...form, firstName: e.target.value})} className="input-field" /></div>
                <div><label className="block text-sm text-gray-600 mb-1">Tên</label><input value={form.lastName} onChange={e => setForm({...form, lastName: e.target.value})} className="input-field" /></div>
              </div>
              <div><label className="block text-sm text-gray-600 mb-1">Email</label><input value={user?.username || ''} className="input-field bg-gray-50" disabled /></div>
              <div><label className="block text-sm text-gray-600 mb-1">Vai trò</label><input value={user?.role || ''} className="input-field bg-gray-50" disabled /></div>
              <button onClick={handleUpdateProfile} disabled={isSaving} className="btn-primary flex items-center gap-2 disabled:opacity-50"><Save className="w-4 h-4" /> Lưu thay đổi</button>
            </div>
          )}

          {tab === 'address' && (
            <div>
              <div className="flex items-center justify-between mb-4">
                <h2 className="font-bold text-gray-800">Địa chỉ giao hàng</h2>
                <button onClick={() => setShowAddressForm(!showAddressForm)} className="btn-primary text-sm flex items-center gap-1">
                  {showAddressForm ? <><X className="w-4 h-4" /> Hủy</> : <><Plus className="w-4 h-4" /> Thêm</>}
                </button>
              </div>
              {showAddressForm && (
                <div className="bg-gray-50 rounded-lg p-4 mb-4 space-y-3 animate-slide-up">
                  <div className="grid grid-cols-2 gap-3">
                    <input value={addressForm.receiverName} onChange={e => setAddressForm({...addressForm, receiverName: e.target.value})} placeholder="Tên người nhận" className="input-field" />
                    <input value={addressForm.phone} onChange={e => setAddressForm({...addressForm, phone: e.target.value})} placeholder="Số điện thoại" className="input-field" />
                  </div>
                  <div className="grid grid-cols-3 gap-3">
                    <select onChange={handleProvinceChange} className="input-field" value={addressForm.provinceId || ''}>
                      <option value="">Tỉnh/Thành</option>
                      {provinces.map(p => <option key={p.provinceId} value={p.provinceId}>{p.provinceName}</option>)}
                    </select>
                    <select onChange={handleDistrictChange} className="input-field" value={addressForm.districtId || ''}>
                      <option value="">Quận/Huyện</option>
                      {districts.map(d => <option key={d.districtId} value={d.districtId}>{d.districtName}</option>)}
                    </select>
                    <select onChange={handleWardChange} className="input-field" value={addressForm.wardId || ''}>
                      <option value="">Phường/Xã</option>
                      {wards.map(w => <option key={w.wardCode} value={w.wardCode}>{w.wardName}</option>)}
                    </select>
                  </div>
                  <input value={addressForm.info} onChange={e => setAddressForm({...addressForm, info: e.target.value})} placeholder="Địa chỉ chi tiết (số nhà, đường...)" className="input-field" />
                  <label className="flex items-center gap-2 text-sm text-gray-600">
                    <input type="checkbox" checked={addressForm.isDefault} onChange={e => setAddressForm({...addressForm, isDefault: e.target.checked})} className="rounded" /> Đặt làm mặc định
                  </label>
                  <button onClick={handleSaveAddress} disabled={isSaving} className="btn-primary text-sm disabled:opacity-50">{isSaving ? 'Đang lưu...' : 'Lưu địa chỉ'}</button>
                </div>
              )}
              <div className="space-y-3">
                {addresses.map(addr => (
                  <div key={addr.id} className="border rounded-lg p-4 flex justify-between">
                    <div>
                      <p className="font-medium text-gray-800 text-sm">{addr.receiverName} — {addr.phone} {(addr.isDefault || addr.isIsDefault) && <span className="badge badge-delivered text-[10px] ml-1">Mặc định</span>}</p>
                      <p className="text-sm text-gray-500 mt-0.5">{addr.info}, {addr.ward}, {addr.district}, {addr.province}</p>
                    </div>
                    <button onClick={() => handleDeleteAddress(addr.id)} className="text-gray-400 hover:text-red-500 transition-colors"><Trash2 className="w-4 h-4" /></button>
                  </div>
                ))}
                {addresses.length === 0 && <p className="text-sm text-gray-400 py-4 text-center">Chưa có địa chỉ nào</p>}
              </div>
            </div>
          )}

          {tab === 'password' && (
            <div className="space-y-4 max-w-sm">
              <h2 className="font-bold text-gray-800 mb-4">Đổi mật khẩu</h2>
              <div><label className="block text-sm text-gray-600 mb-1">Mật khẩu cũ</label><input type="password" value={passwordForm.oldPassword} onChange={e => setPasswordForm({...passwordForm, oldPassword: e.target.value})} className="input-field" /></div>
              <div><label className="block text-sm text-gray-600 mb-1">Mật khẩu mới</label><input type="password" value={passwordForm.newPassword} onChange={e => setPasswordForm({...passwordForm, newPassword: e.target.value})} className="input-field" /></div>
              <div><label className="block text-sm text-gray-600 mb-1">Xác nhận mật khẩu</label><input type="password" value={passwordForm.confirmPassword} onChange={e => setPasswordForm({...passwordForm, confirmPassword: e.target.value})} className="input-field" /></div>
              <button onClick={handleChangePassword} disabled={isSaving} className="btn-primary disabled:opacity-50">{isSaving ? 'Đang xử lý...' : 'Đổi mật khẩu'}</button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
