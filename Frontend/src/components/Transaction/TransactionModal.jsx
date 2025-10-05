import React, { useState } from 'react';
import { X, ChevronDown, CheckCircle, AlertCircle, Loader2 } from 'lucide-react';
import { getAccountByNumber } from '../../api/AccountsApi';
import { set } from 'react-hook-form';
import { getCustomerDetails } from '../../api/customerApi';

const TransactionModal = ({ accounts, onClose, onSubmit }) => {
  const [formData, setFormData] = useState({
    fromAccountNumber: '',
    toAccountNumber: '',
    ifscCode: '',
    recipientName: '',
    amount: '',
    description: ''
  });
  const [showAccountDropdown, setShowAccountDropdown] = useState(false);
  const [accountVerification, setAccountVerification] = useState({
    status: 'idle', // 'idle' | 'verifying' | 'verified' | 'failed'
    recipientName: '',
    branchName: ''
  });
  const [showTransactionForm, setShowTransactionForm] = useState(false);
  const [toAccount, setToAccount] = useState(null);
  // Mock account verification function
  const verifyAccount = async (accountNumber) => {
    setAccountVerification({ status: 'verifying' });

    getAccountByNumber(accountNumber)
      .then(response => {
        
        const account = response.data;
        if (account) {
          setToAccount(account);
          getCustomerDetails(account.customerId)
            .then(res => {
              const customer = res.data;
              console.log(customer)
              setAccountVerification({
                status: 'verified',
                recipientName: customer.name,
                branchName: account.branchName
              });
              setFormData(prev => ({ ...prev, recipientName: customer.name }));
              setShowTransactionForm(true);
            })
            .catch(() => {
              setAccountVerification({ status: 'failed' });
            });
        } else {
          setAccountVerification({ status: 'failed' });
        }
      })
      .catch(() => {
        setAccountVerification({ status: 'failed' });
      });

    if (foundAccount) {
      setAccountVerification({
        status: 'verified',
        recipientName: foundAccount.name,
        bankName: foundAccount.bank
      });
      setFormData(prev => ({ ...prev, recipientName: foundAccount.name }));
      setShowTransactionForm(true);
    } else {
      setAccountVerification({ status: 'failed' });
    }
  };

  const handleAccountVerification = () => {
    if (!formData.toAccountNumber) {
      alert('Please enter account number');
      return;
    }
    verifyAccount(formData.toAccountNumber, formData.ifscCode);
  };

  const resetVerification = () => {
    setAccountVerification({ status: 'idle' });
    setShowTransactionForm(false);
    setFormData(prev => ({ ...prev, recipientName: '' }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!formData.fromAccountNumber || !formData.toAccountNumber || !formData.amount || !formData.description) {
      alert('Please fill in all required fields');
      return;
    }

    if (parseFloat(formData.amount) <= 0) {
      alert('Please enter a valid amount');
      return;
    }

    onSubmit(formData);
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'INR'
    }).format(amount);
  };

  const selectedAccount = accounts.find(acc => formData.fromAccountNumber === `${acc.accountNumber} - ${acc.accountName}`);

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-100">
          <h2 className="text-xl font-semibold text-gray-900">New Transaction</h2>
          <button
            onClick={onClose}
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <X className="w-5 h-5 text-gray-500" />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          {/* To Account Number */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              To Account Number <span className="text-red-500">*</span>
            </label>
            <input
              type="text"
              value={formData.toAccountNumber}
              onChange={(e) => {
                setFormData(prev => ({ ...prev, toAccountNumber: e.target.value }));
                if (accountVerification.status !== 'idle') {
                  resetVerification();
                }
              }}
              placeholder="Enter recipient account number"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              disabled={accountVerification.status === 'verifying'}
            />
          </div>

          {/* IFSC Code */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              IFSC Code <span className="text-red-500">*</span>
            </label>
            <input
              type="text"
              value={formData.ifscCode}
              onChange={(e) => {
                setFormData(prev => ({ ...prev, ifscCode: e.target.value.toUpperCase() }));
                if (accountVerification.status !== 'idle') {
                  resetVerification();
                }
              }}
              placeholder="Enter IFSC code"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              disabled={accountVerification.status === 'verifying'}
            />
          </div>

          {/* Account Verification Button */}
          {accountVerification.status === 'idle' && (
            <div className="flex justify-center">
              <button
                type="button"
                onClick={handleAccountVerification}
                disabled={!formData.toAccountNumber || !formData.ifscCode}
                className="px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white rounded-lg hover:from-blue-700 hover:to-indigo-700 transition-all font-medium disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Verify Account
              </button>
            </div>
          )}

          {/* Verification Status */}
          {accountVerification.status === 'verifying' && (
            <div className="flex items-center justify-center p-4 bg-blue-50 rounded-lg">
              <Loader2 className="w-5 h-5 text-blue-600 animate-spin mr-3" />
              <span className="text-blue-800 font-medium">Verifying account details...</span>
            </div>
          )}

          {accountVerification.status === 'verified' && (
            <div className="p-4 bg-green-50 rounded-lg border border-green-200">
              <div className="flex items-center mb-2">
                <CheckCircle className="w-5 h-5 text-green-600 mr-2" />
                <span className="text-green-800 font-medium">Account Verified Successfully</span>
              </div>
              <div className="text-sm text-green-700">
                <p><strong>Account Holder:</strong> {accountVerification.recipientName}</p>
                <p><strong>Branch:</strong> {accountVerification.branchName}</p>
              </div>
            </div>
          )}

          {accountVerification.status === 'failed' && (
            <div className="p-4 bg-red-50 rounded-lg border border-red-200">
              <div className="flex items-center mb-2">
                <AlertCircle className="w-5 h-5 text-red-600 mr-2" />
                <span className="text-red-800 font-medium">Account Verification Failed</span>
              </div>
              <p className="text-sm text-red-700">
                The account number or IFSC code you entered could not be verified. Please check and try again.
              </p>
              <button
                type="button"
                onClick={resetVerification}
                className="mt-2 text-sm text-red-600 hover:text-red-800 font-medium"
              >
                Try Again
              </button>
            </div>
          )}

          {/* Transaction Form - Only show after verification */}
          {showTransactionForm && (
            <>
              {/* From Account Selection */}
              <div className="relative">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  From Account <span className="text-red-500">*</span>
                </label>
                <div className="relative">
                  <button
                    type="button"
                    onClick={() => setShowAccountDropdown(!showAccountDropdown)}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg text-left bg-white hover:bg-gray-50 transition-colors flex items-center justify-between"
                  >
                    <span className={formData.fromAccountNumber ? 'text-gray-900' : 'text-gray-500'}>
                      {formData.fromAccountNumber || 'Select account'}
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
                            setFormData(prev => ({ ...prev, fromAccountNumber: `${account.accountNumber}` }));
                            setShowAccountDropdown(false);
                          }}
                          className="w-full px-4 py-3 text-left hover:bg-gray-50 transition-colors border-b border-gray-100 last:border-b-0"
                        >
                          <div className="flex justify-between items-center">
                            <div>
                              <div className="font-medium text-gray-900">{account.accountName}</div>
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
                      Available Balance: <span className="font-semibold">{formatCurrency(selectedAccount.balance)}</span>
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
                  <span className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500 font-medium">$</span>
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
                {selectedAccount && formData.amount && parseFloat(formData.amount) > selectedAccount.balance && (
                  <p className="mt-1 text-sm text-red-600">Insufficient balance</p>
                )}
              </div>

              {/* Description */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Description <span className="text-red-500">*</span>
                </label>
                <textarea
                  value={formData.description}
                  onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
                  placeholder="Enter transaction description"
                  rows={3}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all resize-none"
                />
              </div>
            </>
          )}

          {/* Action Buttons - Only show after verification */}
          {showTransactionForm && (
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
                disabled={!formData.fromAccountNumber || !formData.toAccountNumber || !formData.amount || !formData.description || (selectedAccount && parseFloat(formData.amount || '0') > selectedAccount.balance)}
                className="flex-1 px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white rounded-lg hover:from-blue-700 hover:to-indigo-700 transition-all font-medium disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Continue
              </button>
            </div>
          )}

          {/* Cancel button for non-verified state */}
          {!showTransactionForm && (
            <div className="flex justify-center pt-2">
              <button
                type="button"
                onClick={onClose}
                className="px-6 py-3 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors font-medium"
              >
                Cancel
              </button>
            </div>
          )}
        </form>
      </div>
    </div>
  );
};

export default TransactionModal;
