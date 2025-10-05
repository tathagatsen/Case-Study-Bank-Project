import React, { useState, useRef, useEffect } from "react";
import { X, Shield, Eye, EyeOff } from "lucide-react";

const PinModal = ({ onClose, onSubmit, accountData, isLoading }) => {
  const [pin, setPin] = useState(["", "", "", ""]);
  const [showPin, setShowPin] = useState(false);
  const inputRefs = useRef([]);

  useEffect(() => {
    if (inputRefs.current[0]) {
      inputRefs.current[0].focus();
    }
  }, []);

  const handlePinChange = (index, value) => {
    if (!/^\d*$/.test(value)) return;
    const newPin = [...pin];
    newPin[index] = value.slice(-1);
    setPin(newPin);
    if (value && index < 3) {
      inputRefs.current[index + 1]?.focus();
    }
  };
  const handleKeyDown = (index, e) => {
    if (e.key === "Backspace" && !pin[index] && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
  };
  const handleSubmit = (e) => {
    e.preventDefault();
    if (pin.join("").length !== 4) return;
    onSubmit({ ...accountData, pin: pin.join("") });
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-sm">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-100">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-gradient-to-r from-blue-500 to-indigo-600 rounded-lg flex items-center justify-center">
              <Shield className="w-5 h-5 text-white" />
            </div>
            <h2 className="text-xl font-semibold text-gray-900">Confirm PIN</h2>
          </div>
          <button
            onClick={onClose}
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <X className="w-5 h-5 text-gray-500" />
          </button>
        </div>
        {/* Content */}
        <div className="p-6">
          {/* Account Summary */}
          <div className="mb-6 p-4 bg-gray-50 rounded-lg">
            <div className="text-sm text-gray-600 mb-2">Account Summary</div>
            <div className="space-y-2">
              <div className="flex justify-between">
                <span className="text-sm text-gray-700">Account Type:</span>
                <span className="text-sm font-medium text-gray-900">
                  {accountData?.accountType}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-700">Holder Name:</span>
                <span className="text-sm font-medium text-gray-900">
                  {accountData?.accountHolderName}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-700">Branch ID:</span>
                <span className="text-sm font-medium text-gray-900">
                  {accountData?.branchId}
                </span>
              </div>
              {/* <div className="flex justify-between">
                <span className="text-sm text-gray-700">Initial Balance:</span>
                <span className="text-sm font-semibold text-green-600">
                  ₹{parseFloat(accountData?.initialBalance || 0).toFixed(2)}
                </span>
              </div> */}
            </div>
          </div>
          {/* PIN Input Form */}
          <form onSubmit={handleSubmit}>
            <div className="mb-6">
              <div className="flex items-center justify-between mb-4">
                <label className="text-sm font-medium text-gray-700">
                  Enter a 4-digit PIN
                </label>
                <button
                  type="button"
                  onClick={() => setShowPin(!showPin)}
                  className="p-1 hover:bg-gray-100 rounded transition-colors"
                >
                  {showPin ? (
                    <EyeOff className="w-4 h-4 text-gray-500" />
                  ) : (
                    <Eye className="w-4 h-4 text-gray-500" />
                  )}
                </button>
              </div>
              <div className="flex gap-3 justify-center">
                {pin.map((digit, index) => (
                  <input
                    key={index}
                    ref={(el) => (inputRefs.current[index] = el)}
                    type={showPin ? "text" : "password"}
                    value={digit}
                    onChange={(e) => handlePinChange(index, e.target.value)}
                    onKeyDown={(e) => handleKeyDown(index, e)}
                    className="w-14 h-14 text-center text-xl font-semibold border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                    maxLength={1}
                  />
                ))}
              </div>
              <div className="text-xs text-gray-500 text-center mt-3">
                For demo, you can use PIN: 1234
              </div>
            </div>
            {/* Action Buttons */}
            <div className="flex gap-3">
              <button
                type="button"
                onClick={onClose}
                disabled={isLoading}
                className="flex-1 px-6 py-3 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors font-medium disabled:opacity-50"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={pin.join("").length !== 4 || isLoading}
                className="flex-1 px-6 py-3 bg-gradient-to-r from-green-600 to-emerald-600 text-white rounded-lg hover:from-green-700 hover:to-emerald-700 transition-all font-medium disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? (
                  <div className="flex items-center justify-center">
                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin mr-2"></div>
                    Creating...
                  </div>
                ) : (
                  "Create Account"
                )}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PinModal;
