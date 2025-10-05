import React, { useState } from 'react';
import { X, ChevronDown } from 'lucide-react';

const TransactionModal = ({ accounts, onClose, onSubmit }) => {
  const [formData, setFormData] = useState({
    toAccountNumber: '',
    amount: '',
    description: ''
  });
  const [showAccountDropdown, setShowAccountDropdown] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    const amt = parseFloat(formData.amount || '0');

    if (!formData.toAccountNumber || !formData.amount || !formData.description) {
      alert('Please fill in all required fields');
      return;
    }
    if (isNaN(amt) || amt <= 0) {
      alert('Please enter a valid amount');
      return;
    }

    // Parent should open PIN modal next with this payload
    onSubmit({
      type: 'DEPOSIT',
      toAccountNumber: formData.toAccountNumber,
      amount: amt,
      description: formData.description
    });
  };

  const formatCurrency = (amount) => {
    try {
      return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);
    } catch {
      return `₹${amount}`;
    }
  };

  const selectedAccount = accounts.find(acc => formData.toAccountNumber === String(acc.accountNumber));

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-100">
          <h2 className="text-xl font-semibold text-gray-900">New Deposit</h2>
          <button
            onClick={onClose}
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <X className="w-5 h-5 text-gray-500" />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          {/* Account to deposit into */}
          <div className="relative">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Deposit To Account <span className="text-red-500">*</span>
            </label>
            <div className="relative">
              <button
                type="button"
                onClick={() => setShowAccountDropdown(!showAccountDropdown)}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg text-left bg-white hover:bg-gray-50 transition-colors flex items-center justify-between"
              >
                <span className={formData.toAccountNumber ? 'text-gray-900' : 'text-gray-500'}>
                  {formData.toAccountNumber || 'Select account'}
                </span>
                <ChevronDown className={`w-5 h-5 text-gray-400 transition-transform ${showAccountDropdown ? 'rotate-180' : ''}`} />
              </button>

              {showAccountDropdown && (
                <div className="absolute top-full left-0 right-0 mt-1 bg-white border border-gray-200 rounded-lg shadow-lg z-10">
                  {accounts.map((account) => (
                    <button
                      key={account.id}
                      type="button"
                      onClick={() => {
                        setFormData(prev => ({ ...prev, toAccountNumber: String(account.accountNumber) }));
                        setShowAccountDropdown(false);
                      }}
                      className="w-full px-4 py-3 text-left hover:bg-gray-50 transition-colors border-b border-gray-100 last:border-b-0"
                    >
                      <div className="flex justify-between items-center">
                        <div>
                          <div className="font-medium text-gray-900">{account.accountName || account.accountType}</div>
                          <div className="text-sm text-gray-500">{account.accountNumber}</div>
                        </div>
                        <div className="text-sm font-medium text-gray-900">
                          {formatCurrency(account.balance)}
                        </div>
                      </div>
                    </button>
                  ))}
                </div>
              )}
            </div>

            {selectedAccount && (
              <div className="mt-2 p-3 bg-blue-50 rounded-lg">
                <div className="text-sm text-blue-800">
                  Current Balance: <span className="font-semibold">{formatCurrency(selectedAccount.balance)}</span>
                </div>
              </div>
            )}
          </div>

          {/* Amount */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Amount <span className="text-red-500">*</span>
            </label>
            <div className="relative">
              <span className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500 font-medium">₹</span>
              <input
                type="number"
                step="0.01"
                min="0.01"
                value={formData.amount}
                onChange={(e) => setFormData(prev => ({ ...prev, amount: e.target.value }))}
                placeholder="0.00"
                className="w-full pl-8 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              />
            </div>
          </div>

          {/* Description */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Description <span className="text-red-500">*</span>
            </label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
              placeholder="Enter deposit description"
              rows={3}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all resize-none"
            />
          </div>

          {/* Actions */}
          <div className="flex gap-3 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-6 py-3 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors font-medium"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={!formData.toAccountNumber || !formData.amount || !formData.description}
              className="flex-1 px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white rounded-lg hover:from-blue-700 hover:to-indigo-700 transition-all font-medium disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Continue
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default TransactionModal;