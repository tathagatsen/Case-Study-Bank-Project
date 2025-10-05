import React from "react";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { verifyLogin } from "../../api/UserApi"; // <-- adjust import if needed
import { useNavigate } from "react-router";
import { useDispatch } from "react-redux";
import { setUser } from "../../redux/Slice/User";

function setAuthCookie(email, role) {
  const expires = new Date(Date.now() + 60 * 60 * 1000).toUTCString();
  document.cookie = `pqr_auth=${encodeURIComponent(email)}; expires=${expires}; path=/`;
  document.cookie = `pqr_auth_role=${encodeURIComponent(role)}; expires=${expires}; path=/`;
}

export default function OtpModal({ open, email, onClose }) {
  const { register, handleSubmit, formState: { errors } } = useForm();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  if (!open) return null; // Don't render if modal is closed

  const onSubmit = (data) => {
    // Send email and otp to backend
    console.log("Verifying OTP for:", email, data.otp);
    verifyLogin({ email, otp: data.otp })
      .then(res => {
        console.log("OTP verification response:", res.data);
        if (res && res.data) {
            // Update Redux store with user data
          dispatch(setUser(res.data));
          setAuthCookie(res.data.email, res.data.role);
          toast.success("Login verified!");
          onClose();
          navigate("/dashboard");
        } else {
          toast.error("OTP verification failed!");
        }
      })
      .catch(() => {
        toast.error("Invalid OTP or server error.");
      });
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30 z-50">
      <div className="bg-white p-6 rounded-lg shadow-xl max-w-sm w-full">
        <h2 className="text-xl font-bold mb-4 text-center">Enter OTP</h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div>
            <label htmlFor="otp" className="block text-sm font-medium text-gray-700">
              OTP sent to {email}
            </label>
            <input
              id="otp"
              type="text"
              maxLength={6} // or adjust as per your OTP 
              {...register("otp", { required: "OTP is required" })}
              className="mt-2 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            />
            {errors.otp && (
              <span className="text-red-500 text-xs">{errors.otp.message}</span>
            )}
          </div>
          <div className="flex justify-between space-x-4 mt-4">
            <button
              type="button"
              className="py-2 px-4 rounded bg-gray-200 text-gray-600"
              onClick={onClose}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="py-2 px-4 rounded bg-indigo-600 text-white"
            >
              Verify
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}