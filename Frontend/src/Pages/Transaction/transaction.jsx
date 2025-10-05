import React, { useEffect, useRef, useState } from 'react';
import { Search, Plus, ArrowUpRight, ArrowDownLeft } from 'lucide-react';
import TransactionModal from '../../components/Transaction/TransactionModal';
import PinVerificationModal from '../../components/Transaction/PinVerficationModal';
import TransactionDetailsModal from '../../components/Transaction/TransactionDetailModal';
import { useSelector } from 'react-redux';
import { useAppDispatch } from '../../redux/store';
import { getTransactionsForCustomer, getAccountsByCustomer } from '../../api/AccountsApi';
import toast from "react-hot-toast";
import { replaceAccounts } from '../../redux/Slice/AccountSlice';

const transaction = () => {
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
  }, [transactionCompleted]);

 useEffect(() => {
  getTransactionsForCustomer(user.userId)
    .then((response) => {
      console.log("Fetched transactions for customer:", response.data);
      
      const userAccountNumbers = accounts.map(acct => acct.accountNumber);
      let mapped = response.data.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      mapped = mapped.filter(txn =>
        (txn.type != "DEPOSIT")
       );
      mapped = response.data.map(txn => {
        let mappedType, accountNumber, recipient;

        const fromIsUser = userAccountNumbers.includes(txn.fromAccountNumber);
        const toIsUser = userAccountNumbers.includes(txn.toAccountNumber);

        console.log({ fromIsUser, toIsUser, txn });

        if (txn.type === "DEPOSIT") {
          mappedType = "credit";
          accountNumber = txn.toAccountNumber;
          recipient = txn.description || txn.fromAccountNumber || "Deposit";
        } else if (txn.type === "WITHDRAWAL") {
          mappedType = "debit";
          accountNumber = txn.fromAccountNumber;
          recipient = txn.description || txn.toAccountNumber || "Withdrawal";
        } else if (txn.type === "TRANSFER") {
          if (fromIsUser && toIsUser) {
            // Self transfer
            mappedType = "self";
            accountNumber = txn.fromAccountNumber;
            recipient = `To ${txn.toAccountNumber}`;
          } else if (fromIsUser) {
            mappedType = "debit";
            accountNumber = txn.fromAccountNumber;
            recipient = txn.toAccountNumber;
          } else if (toIsUser) {
            mappedType = "credit";
            accountNumber = txn.fromAccountNumber;
            recipient = txn.toAccountNumber;
          } else {
            mappedType = "debit";
            accountNumber = txn.fromAccountNumber;
            recipient = txn.toAccountNumber;
          }
        }

        // Mask last 4 digits of account for UI
        function maskAccount(accnum) {
          if (!accnum) return "";
          return `****${accnum.slice(-4)}`;
        }

        return {
          id: String(txn.transactionId),
          type: mappedType, // will be 'credit', 'debit', or 'self'
          amount: txn.amount,
          description: txn.description,
          date: txn.createdAt || "",
          accountNumber: accountNumber,
          recipient: recipient
        };
      });

      setTransactions(mapped);
    })
    .catch((err) => console.error("Error fetching transactions for customer:", err));
}, [transactionCompleted]);


  const filteredTransactions = transactions.filter(
    (transaction) =>
      transaction.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      transaction.recipient?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      transaction.accountNumber.includes(searchTerm)
  );

  const handleTransactionSubmit = (data) => {
    setTransactionData(data);
    setShowTransactionModal(false);
    setShowPinModal(true);
    
  };

  const handlePinVerification = (response) => {
    // Simulate PIN verification (PIN: 1234)

      if(response) {
        
      setShowPinModal(false);
      setTransactionData(null);
      setTransactionCompleted(prev => !prev);
      toast.success('Transaction successful!');
      

    } else {
      toast.error('Invalid PIN. Transaction failed.');
    }
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'INR'
    }).format(amount);
  };

  const formatDate = (dateString) => {
  return new Date(dateString).toLocaleString('en-US', {
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
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Transactions</h1>
          <p className="text-gray-600">Manage your account transactions and make new payments</p>
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
              placeholder="Search transactions..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-3 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
            />
          </div>

          {/* Make Transaction Button */}
          <button
            onClick={() => setShowTransactionModal(true)}
            className="inline-flex items-center gap-2 bg-gradient-to-r from-blue-600 to-indigo-600 text-white px-6 py-3 rounded-lg hover:from-blue-700 hover:to-indigo-700 transition-all duration-200 shadow-md hover:shadow-lg"
          >
            <Plus className="w-5 h-5" />
            Make Transaction
          </button>
        </div>
        {/* Transactions List */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden" >
          <div className="px-6 py-4 border-b border-gray-100">
            <h2 className="text-lg font-semibold text-gray-900">Recent Transactions</h2>
          </div>

          <div className="divide-y divide-gray-100" >
            {filteredTransactions.length === 0 ? (
              <div className="px-6 py-12 text-center">
                <div className="text-gray-400 mb-2">
                  <Search className="w-12 h-12 mx-auto" />
                </div>
                <p className="text-gray-500">No transactions found</p>
              </div>
            ) : (
              filteredTransactions.map((transaction) => (
                <div
                  key={transaction.transactionId}
                  className="px-6 py-4 hover:bg-gray-50 transition-colors"
                >
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-4">
                      <div
                        className={`w-12 h-12 rounded-lg flex items-center justify-center ${
                          transaction.type === 'credit'
                            ? 'bg-green-100 text-green-600'
                            : transaction.type === 'self' 
                              ? 'bg-gray-100 text-gray-600'
                              : 'bg-red-100 text-red-600'
                        }`}
                      >
                        {transaction.type === 'credit' ? (
                          <ArrowDownLeft className="w-6 h-6" />
                        ) : (
                          <ArrowUpRight className="w-6 h-6" />
                        )}
                      </div>
                      <div>
                        <h3 className="font-semibold text-gray-900">{transaction.description}</h3>
                        <div className="flex items-center gap-2 text-sm text-gray-500">
                          <span>{transaction.recipient}</span>
                          <span>•</span>
                          <span>FROM {transaction.accountNumber}</span>
                        </div>
                      </div>
                    </div>
                    <div className="text-right">
                      <div
                        className={`text-lg font-semibold ${
                          transaction.type === 'credit'
                            ? ' text-green-600'
                            : transaction.type === 'self' 
                              ? ' text-gray-600'
                              : ' text-red-600'
                        }`}
                      >
                        {transaction.type === 'credit' ? '+' : transaction.type === 'self' ? '' : '-'}
                        {formatCurrency(transaction.amount)}
                      </div>
                      <div className="text-sm text-gray-500">{formatDate(transaction.date)}</div>
                      <button
                  onClick={() => setSelectedTransaction(transaction)}
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
        />
      )}

       {/* Modal Component */}
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

export default transaction;
