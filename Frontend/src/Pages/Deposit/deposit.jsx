import React, { useEffect, useState } from 'react';
import { Search, Plus, ArrowDownLeft } from 'lucide-react';
import TransactionModal from '../../components/Deposit/TransactionModal';
import PinVerificationModal from '../../components/Deposit/PinVerficationModal';
import TransactionDetailsModal from '../../components/Deposit/TransactionDetailModal';
import { useSelector } from 'react-redux';
import { useAppDispatch } from '../../redux/store';
import { getTransactionsForCustomer, getAccountsByCustomer } from '../../api/AccountsApi';
import toast from "react-hot-toast";
import { replaceAccounts } from '../../redux/Slice/AccountSlice';

const Deposit = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [showTransactionModal, setShowTransactionModal] = useState(false);
  const [showPinModal, setShowPinModal] = useState(false);
  const [transactionData, setTransactionData] = useState(null);
  const [selectedTransaction, setSelectedTransaction] = useState(null);

  const accounts = useSelector((state) => state.accounts.list);
  const [transactions, setTransactions] = useState([]);
  const user = useSelector((state) => state.user);
  const dispatch = useAppDispatch();
  const [transactionCompleted, setTransactionCompleted] = useState(false);

  useEffect(() => {
    getAccountsByCustomer(user.userId)
      .then((response) => {
        dispatch(replaceAccounts(response.data));
      })
      .catch((err) => console.error("Error fetching accounts for customer:", err));
  }, [dispatch, user.userId, transactionCompleted]);

  useEffect(() => {
    getTransactionsForCustomer(user.userId)
      .then((response) => {
        // Only DEPOSIT transactions
        const onlyDeposits = (Array.isArray(response.data) ? response.data : [])
          .filter(txn => txn.type === 'DEPOSIT')
          .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

        const userAccountNumbers = (accounts || []).map(acct => acct.accountNumber);

        const mapped = onlyDeposits.map(txn => {
          // For DEPOSIT: money coming into user's account
          // Choose fields: credit to toAccountNumber
          const accountNumber = txn.toAccountNumber;
          const recipient = txn.description || txn.fromAccountNumber || "Deposit";

          return {
            id: String(txn.transactionId),
            type: 'credit', // deposits are credits
            amount: txn.amount,
            description: txn.description || 'Deposit',
            date: txn.createdAt || "",
            accountNumber,
            recipient
          };
        });

        setTransactions(mapped);
      })
      .catch((err) => console.error("Error fetching transactions for customer:", err));
  }, [user.userId, accounts, transactionCompleted]);

  const filteredTransactions = transactions.filter(t =>
    (t.description || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
    (t.recipient || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
    (t.accountNumber || '').includes(searchTerm)
  );

  const handleTransactionSubmit = (data) => {
    // Force deposit type by default if your modal supports it; otherwise ensure your modal sends type: 'DEPOSIT'.
    setTransactionData({ ...data, type: 'DEPOSIT' });
    setShowTransactionModal(false);
    setShowPinModal(true);
  };

  const handlePinVerification = (response) => {
    if (response) {
      setShowPinModal(false);
      setTransactionData(null);
      setTransactionCompleted(prev => !prev);
      toast.success('Deposit successful!');
    } else {
      toast.error('Invalid PIN. Deposit failed.');
    }
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR'
    }).format(amount);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString('en-IN', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: '2-digit',
      hour12: true
    });
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50">
      <div className="container mx-auto px-4 py-8 max-w-6xl">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Deposits</h1>
          <p className="text-gray-600">View your deposit history and make new deposits</p>
        </div>

        {/* Account Summary Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          {accounts.map((account) => (
            <div
              key={account.id}
              className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 hover:shadow-md transition-shadow"
            >
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h3 className="font-semibold text-gray-900">{account.accountType}</h3>
                  <p className="text-sm text-gray-500">{account.accountNumber}</p>
                </div>
                <div className="w-12 h-12 bg-gradient-to-r from-blue-500 to-indigo-600 rounded-lg flex items-center justify-center">
                  <div className="w-6 h-6 bg-white rounded-sm"></div>
                </div>
              </div>
              <div className="text-2xl font-bold text-gray-900">{formatCurrency(account.balance)}</div>
            </div>
          ))}
        </div>

        {/* Controls */}
        <div className="flex flex-col sm:flex-row justify-between items-center gap-4 mb-6">
          {/* Search Bar */}
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Search deposits..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-3 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
            />
          </div>

          {/* Make Deposit Button */}
          <button
            onClick={() => setShowTransactionModal(true)}
            className="inline-flex items-center gap-2 bg-gradient-to-r from-blue-600 to-indigo-600 text-white px-6 py-3 rounded-lg hover:from-blue-700 hover:to-indigo-700 transition-all duration-200 shadow-md hover:shadow-lg"
          >
            <Plus className="w-5 h-5" />
            Make Deposit
          </button>
        </div>

        {/* Deposits List */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-100">
            <h2 className="text-lg font-semibold text-gray-900">Recent Deposits</h2>
          </div>
          <div className="divide-y divide-gray-100">
            {filteredTransactions.length === 0 ? (
              <div className="px-6 py-12 text-center">
                <div className="text-gray-400 mb-2">
                  <Search className="w-12 h-12 mx-auto" />
                </div>
                <p className="text-gray-500">No deposits found</p>
              </div>
            ) : (
              filteredTransactions.map((t) => (
                <div
                  key={t.id}
                  className="px-6 py-4 hover:bg-gray-50 transition-colors"
                >
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-4">
                      <div className="w-12 h-12 rounded-lg flex items-center justify-center bg-green-100 text-green-600">
                        <ArrowDownLeft className="w-6 h-6" />
                      </div>
                      <div>
                        <h3 className="font-semibold text-gray-900">{t.description || 'Deposit'}</h3>
                        <div className="flex items-center gap-2 text-sm text-gray-500">
                          <span>{t.recipient}</span>
                          <span>•</span>
                          <span>TO {t.accountNumber}</span>
                        </div>
                      </div>
                    </div>
                    <div className="text-right">
                      <div className="text-lg font-semibold text-green-600">
                        +{formatCurrency(t.amount)}
                      </div>
                      <div className="text-sm text-gray-500">{formatDate(t.date)}</div>
                      <button
                        onClick={() => setSelectedTransaction(t)}
                        className="mt-2 text-sm text-blue-600 hover:underline"
                      >
                        View Details
                      </button>
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </div>

      {/* Modals */}
      {showTransactionModal && (
        <TransactionModal
          accounts={accounts}
          onClose={() => setShowTransactionModal(false)}
          onSubmit={handleTransactionSubmit}
          defaultType="DEPOSIT" // ensure your modal respects this (or set type inside handleTransactionSubmit)
        />
      )}

      <TransactionDetailsModal
        transaction={selectedTransaction}
        onClose={() => setSelectedTransaction(null)}
      />

      {showPinModal && (
        <PinVerificationModal
          onClose={() => {
            setShowPinModal(false);
            setTransactionData(null);
          }}
          onVerify={handlePinVerification}
          transactionData={transactionData}
        />
      )}
    </div>
  );
};

export default Deposit;