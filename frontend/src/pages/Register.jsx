import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, Lock, User, Eye, EyeOff } from 'lucide-react';
import toast from 'react-hot-toast';
import authApi from '../api/authApi';

export default function Register() {
  const [step, setStep] = useState(1);
  const [form, setForm] = useState({ email: '', password: '', confirmPassword: '', firstName: '', lastName: '' });
  const [otp, setOtp] = useState('');
  const [uuid, setUuid] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleRegister = async (e) => {
    e.preventDefault();
    if (form.password !== form.confirmPassword) {
      toast.error('Mật khẩu xác nhận không khớp');
      return;
    }
    if (form.password.length < 8) {
      toast.error('Mật khẩu phải có ít nhất 8 ký tự');
      return;
    }
    setIsLoading(true);
    try {
      const res = await authApi.register({
        email: form.email,
        password: form.password,
        firstName: form.firstName,
        lastName: form.lastName,
      });
      setUuid(res.data);
      setStep(2);
      toast.success('OTP đã được gửi đến email của bạn');
    } catch {
      toast.error('Đăng ký thất bại. Email có thể đã tồn tại.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      await authApi.verifyOtp({ otp, uuid });
      toast.success('Đăng ký thành công! Vui lòng đăng nhập.');
      navigate('/login');
    } catch {
      toast.error('OTP không đúng hoặc đã hết hạn');
    } finally {
      setIsLoading(false);
    }
  };

  const handleResendOtp = async () => {
    try {
      await authApi.resendOtp({ email: form.email });
      toast.success('OTP mới đã được gửi');
    } catch {
      toast.error('Lỗi gửi lại OTP');
    }
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4 py-12 animate-fade-in">
      <div className="w-full max-w-md">
        <div className="bg-white rounded-2xl shadow-lg p-8">
          <div className="text-center mb-8">
            <div className="w-14 h-14 bg-primary-500 rounded-2xl flex items-center justify-center mx-auto mb-4">
              <span className="text-white font-extrabold text-2xl">S</span>
            </div>
            <h1 className="text-2xl font-bold text-gray-800">{step === 1 ? 'Tạo tài khoản' : 'Xác thực OTP'}</h1>
            <p className="text-sm text-gray-400 mt-1">
              {step === 1 ? 'Đăng ký tài khoản mới tại ShopEase' : `Nhập mã OTP đã gửi đến ${form.email}`}
            </p>
          </div>

          {step === 1 ? (
            <form onSubmit={handleRegister} className="space-y-4">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Họ</label>
                  <div className="relative">
                    <User className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                    <input name="firstName" value={form.firstName} onChange={handleChange} placeholder="Nguyễn" className="input-field pl-9" required />
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Tên</label>
                  <input name="lastName" value={form.lastName} onChange={handleChange} placeholder="Văn A" className="input-field" required />
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <div className="relative">
                  <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                  <input type="email" name="email" value={form.email} onChange={handleChange} placeholder="your@email.com" className="input-field pl-9" required />
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Mật khẩu</label>
                <div className="relative">
                  <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                  <input type={showPassword ? 'text' : 'password'} name="password" value={form.password} onChange={handleChange} placeholder="Tối thiểu 8 ký tự" className="input-field pl-9 pr-10" required />
                  <button type="button" onClick={() => setShowPassword(!showPassword)} className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400">
                    {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                  </button>
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Xác nhận mật khẩu</label>
                <div className="relative">
                  <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                  <input type="password" name="confirmPassword" value={form.confirmPassword} onChange={handleChange} placeholder="Nhập lại mật khẩu" className="input-field pl-9" required />
                </div>
              </div>
              <button type="submit" disabled={isLoading} className="btn-primary w-full disabled:opacity-50">
                {isLoading ? 'Đang xử lý...' : 'Đăng ký'}
              </button>
            </form>
          ) : (
            <form onSubmit={handleVerifyOtp} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Mã OTP</label>
                <input type="text" value={otp} onChange={e => setOtp(e.target.value)} placeholder="Nhập mã 6 số" className="input-field text-center text-2xl tracking-widest" maxLength={6} required />
              </div>
              <button type="submit" disabled={isLoading} className="btn-primary w-full disabled:opacity-50">
                {isLoading ? 'Đang xác thực...' : 'Xác nhận'}
              </button>
              <p className="text-center text-sm text-gray-400">
                Không nhận được mã?{' '}
                <button type="button" onClick={handleResendOtp} className="text-primary-500 hover:underline">Gửi lại</button>
              </p>
            </form>
          )}

          <p className="text-center text-sm text-gray-400 mt-6">
            Đã có tài khoản?{' '}
            <Link to="/login" className="text-primary-500 font-medium hover:underline">Đăng nhập</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
