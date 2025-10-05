import React, { useState, useRef, useEffect } from 'react';
import { X, Shield, Eye, EyeOff } from 'lucide-react';
import { createTransaction } from '../../api/AccountsApi';
import axios from 'axios';

const PinVerificationModal = ({ onClose, onVerify, transactionData }) => {
  const [pin, setPin] = useState(['', '', '', '']);
  const [showPin, setShowPin] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const inputRefs = useRef([]);

  useEffect(() => {
    // Focus on first input when modal opens
    if (inputRefs.current[0]) {
      inputRefs.current[0].focus();
    }
  }, []);

  console.log('Transaction Data:', transactionData);
  const handlePinChange = (index, value) => {
    // Only allow numbers
    if (!/^\d*$/.test(value)) return;

    const newPin = [...pin];
    newPin[index] = value.slice(-1); // Only take the last digit
    setPin(newPin);

    // Auto-focus next input
    if (value && index < 3) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  const handleKeyDown = (index, e) => {
    if (e.key === 'Backspace' && !pin[index] && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const pinString = pin.join('');
    setIsLoading(true);

    const transactionDetails = {
      ...transactionData,
      pin: pinString,
      type: 'TRANSFER' // assuming type is always transfer for this modal
    };
    console.log('Submitting transaction with details:', transactionDetails);
    axios.post("http://localhost:8083/accounts/createTransaction", transactionDetails)
      .then(response => {
        console.log('Transaction successful:', response.data);
        setIsLoading(false);
        onVerify(true);
      })
      .catch(error => {
        console.error('Transaction failed:', error);
      });

    
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'INR'
    }).format(amount);
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
            <h2 className="text-xl font-semibold text-gray-900">Verify PIN</h2>
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
          {/* Transaction Summary */}
          <div className="mb-6 p-4 bg-gray-50 rounded-lg">
            <div className="text-sm text-gray-600 mb-2">Transaction Summary</div>
            <div className="space-y-2">
              <div className="flex justify-between">
                <span className="text-sm text-gray-700">To Account:</span>
                <span className="text-sm font-medium text-gray-900">{transactionData?.toAccountNumber}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-700">Amount:</span>
                <span className="text-sm font-semibold text-red-600">
                  {formatCurrency(parseFloat(transactionData?.amount || '0'))}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-700">Description:</span>
                <span className="text-sm text-gray-900">{transactionData?.description}</span>
              </div>
            </div>
          </div>

          {/* PIN Input Form */}
          <form onSubmit={handleSubmit}>
            <div className="mb-6">
              <div className="flex items-center justify-between mb-4">
                <label className="text-sm font-medium text-gray-700">
                  Enter your 4-digit PIN
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
                    type={showPin ? 'text' : 'password'}
                    value={digit}
                    onChange={(e) => handlePinChange(index, e.target.value)}
                    onKeyDown={(e) => handleKeyDown(index, e)}
                    className="w-14 h-14 text-center text-xl font-semibold border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                    maxLength={1}
                  />
                ))}
              </div>

              <div className="text-xs text-gray-500 text-center mt-3">
                For demo purposes, use PIN: 1234
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
                disabled={pin.join('').length !== 4 || isLoading}
                className="flex-1 px-6 py-3 bg-gradient-to-r from-green-600 to-emerald-600 text-white rounded-lg hover:from-green-700 hover:to-emerald-700 transition-all font-medium disabled:opacity-50 disabled:cursor-not-allowed relative"
              >
                {isLoading ? (
                  <div className="flex items-center justify-center">
                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin mr-2"></div>
                    Processing...
                  </div>
                ) : (
                  'Confirm Transaction'
                )}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PinVerificationModal;
