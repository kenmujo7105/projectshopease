import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Mail, Lock } from 'lucide-react';
import toast from 'react-hot-toast';
import authApi from '../api/authApi';

export default function ForgotPassword() {
  const [step, setStep] = useState(1);
  const [email, setEmail] = useState('');
  const [otp, setOtp] = useState('');
  const [uuid, setUuid] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleSendOtp = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      const res = await authApi.forgotPassword({ email });
      setUuid(res.data);
      setStep(2);
      toast.success('OTP đã được gửi đến email');
    } catch {
      toast.error('Email không tồn tại trong hệ thống');
    } finally {
      setIsLoading(false);
    }
  };

  const handleVerify = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      await authApi.forgotVerifyOtp({ otp, uuid });
      setStep(3);
    } catch {
      toast.error('OTP không đúng');
    } finally {
      setIsLoading(false);
    }
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (newPassword.length < 8) {
      toast.error('Mật khẩu phải có ít nhất 8 ký tự');
      return;
    }
    setIsLoading(true);
    try {
      await authApi.forgotChangePassword({ email, password: newPassword });
      toast.success('Đổi mật khẩu thành công!');
      navigate('/login');
    } catch {
      toast.error('Lỗi đổi mật khẩu');
    } finally {
      setIsLoading(false);
    }
  };

  const titles = { 1: 'Quên mật khẩu', 2: 'Nhập mã OTP', 3: 'Mật khẩu mới' };

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4 py-12 animate-fade-in">
      <div className="w-full max-w-md bg-white rounded-2xl shadow-lg p-8">
        <h1 className="text-2xl font-bold text-gray-800 text-center mb-2">{titles[step]}</h1>

        {step === 1 && (
          <form onSubmit={handleSendOtp} className="mt-6 space-y-4">
            <div className="relative">
              <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
              <input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="Nhập email của bạn" className="input-field pl-9" required />
            </div>
            <button type="submit" disabled={isLoading} className="btn-primary w-full disabled:opacity-50">
              {isLoading ? 'Đang gửi...' : 'Gửi mã xác thực'}
            </button>
          </form>
        )}

        {step === 2 && (
          <form onSubmit={handleVerify} className="mt-6 space-y-4">
            <input type="text" value={otp} onChange={e => setOtp(e.target.value)} placeholder="Nhập mã OTP" className="input-field text-center text-2xl tracking-widest" maxLength={6} required />
            <button type="submit" disabled={isLoading} className="btn-primary w-full disabled:opacity-50">
              {isLoading ? 'Đang xác thực...' : 'Xác nhận'}
            </button>
          </form>
        )}

        {step === 3 && (
          <form onSubmit={handleChangePassword} className="mt-6 space-y-4">
            <div className="relative">
              <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
              <input type="password" value={newPassword} onChange={e => setNewPassword(e.target.value)} placeholder="Mật khẩu mới (tối thiểu 8 ký tự)" className="input-field pl-9" required />
            </div>
            <button type="submit" disabled={isLoading} className="btn-primary w-full disabled:opacity-50">
              {isLoading ? 'Đang xử lý...' : 'Đổi mật khẩu'}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}
