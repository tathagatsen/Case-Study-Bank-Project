import React, { useState, useEffect } from 'react';
import { Search, Plus, Wallet, TrendingUp, Users, CreditCard, Menu } from 'lucide-react';
import AccountCard from '../../components/Account/AccountCard';
import CreateAccountModal from '../../components/Account/CreateAccountModal';
import AccountDetailsModal from '../../components/Account/AccountDetailsModal';
import { useSelector } from 'react-redux';
import { useAppDispatch } from '../../redux/store';
import { replaceAccounts } from '../../redux/Slice/AccountSlice';
import { getAccountsByCustomer } from '../../api/AccountsApi';


function Accounts() {
  const dispatch = useAppDispatch();
  const { list: accounts, loading, error } = useSelector((state) => state.accounts);
  const user = useSelector((state) => state.user);
  const [activeTab, setActiveTab] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [selectedAccount, setSelectedAccount] = useState(null);
  const [accountCreated, setAccountCreated] = useState(false);
  // Debug Redux state
  // console.log("🔹 Redux accounts state:", { accounts, loading, error });

  // Fetch all accounts on mount
  useEffect(() => {
    getAccountsByCustomer(user.userId)
    .then(response => {
      console.log("Fetched accounts:", response.data);
      dispatch(replaceAccounts(response.data));
    })
    .catch(err => console.error("Error fetching accounts:", err));
  }, [accountCreated]);


const getTotalBalance = () => {
  return accounts.reduce((sum, acc) => sum + (acc.balance || 0), 0);
};

    

  console.log("Total Balance:", getTotalBalance());
  
  
  const getAccountsByType = (type) => {
  if (type === 'all') return accounts || [];

  return (accounts || []).filter(acc => 
    (acc.accountType?.toLowerCase() || "") === type.toLowerCase()
  );
};


  const getAccountStats = () => {
    const stats = {
      total: accounts.length,
      active: accounts.filter(acc => acc.status.toLowerCase() === 'active').length,
      byType: {
        savings: accounts.filter(acc => acc.accountType.toLowerCase() === 'savings').length,
        salary: accounts.filter(acc => acc.accountType.toLowerCase() === 'salary').length,
        current: accounts.filter(acc => acc.accountType.toLowerCase() === 'current').length,
      },
    };
    return stats;
  };

  const stats = getAccountStats();
  const totalBalance = getTotalBalance();
  const filteredAccounts = getAccountsByType(activeTab).filter(account =>
  account.accountHolderName?.toLowerCase().includes(searchQuery.toLowerCase()) ||
  account.accountNumber?.includes(searchQuery) ||
  account.branchName?.toLowerCase().includes(searchQuery.toLowerCase())
);

  const formatCurrency = (amount) =>
    new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', minimumFractionDigits: 2 }).format(amount);

  const handleViewDetails = (account) => setSelectedAccount(account);

  const tabs = [
    { id: 'all', label: 'All Accounts', count: stats.total },
    { id: 'salary', label: 'Salary', count: stats.byType.salary },
    { id: 'savings', label: 'Savings', count: stats.byType.savings },
    { id: 'current', label: 'Current', count: stats.byType.current },
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <button className="md:hidden"><Menu className="w-6 h-6 text-gray-600" /></button>
            <div className="flex items-center space-x-2">
              <Wallet className="w-8 h-8 text-blue-600" />
              <h1 className="text-xl font-semibold text-gray-900">Account Overview</h1>
            </div>
          </div>
          <div className="flex items-center space-x-4">
            <div className="relative hidden md:block">
              <Search className="w-5 h-5 text-gray-400 absolute left-3 top-1/2 transform -translate-y-1/2" />
              <input
                type="text"
                placeholder="Search transactions, accounts..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent w-80"
              />
            </div>
            <button
              onClick={() => setIsCreateModalOpen(true)}
              className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center space-x-2"
            >
              <Plus className="w-4 h-4" />
              <span>Open New Account</span>
            </button>
          </div>
        </div>
      </header>

      <div className="px-6 py-8">
        {/* Statistics Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {/* Total Balance */}
          <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-sm font-medium text-gray-600 mb-2">Total Balance</h3>
                <p className="text-2xl font-bold text-gray-900">{formatCurrency(totalBalance)}</p>
                <p className="text-sm text-green-600 flex items-center mt-1">
                  <TrendingUp className="w-4 h-4 mr-1" />
                  Balance
                </p>
              </div>
              <div className="p-3 bg-blue-100 rounded-lg"><Wallet className="w-6 h-6 text-blue-600" /></div>
            </div>
          </div>

          {/* Active Accounts */}
          <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-sm font-medium text-gray-600 mb-2">Active Accounts</h3>
                <p className="text-2xl font-bold text-gray-900">{stats.active}</p>
                <p className="text-sm text-gray-500">{stats.total} total accounts</p>
              </div>
              <div className="p-3 bg-green-100 rounded-lg"><Users className="w-6 h-6 text-green-600" /></div>
            </div>
          </div>

          {/* Savings Accounts */}
          <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-sm font-medium text-gray-600 mb-2">Savings Accounts</h3>
                <p className="text-2xl font-bold text-gray-900">{stats.byType.savings}</p>
                <p className="text-sm text-gray-500">High interest earning</p>
              </div>
              <div className="p-3 bg-emerald-100 rounded-lg"><TrendingUp className="w-6 h-6 text-emerald-600" /></div>
            </div>
          </div>

          {/* Business Accounts */}
          <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-sm font-medium text-gray-600 mb-2">Business Accounts</h3>
                <p className="text-2xl font-bold text-gray-900">{stats.byType.current + stats.byType.salary}</p>
                <p className="text-sm text-gray-500">Salary + Current</p>
              </div>
              <div className="p-3 bg-purple-100 rounded-lg"><CreditCard className="w-6 h-6 text-purple-600" /></div>
            </div>
          </div>
        </div>

        {/* Tab Navigation */}
        <div className="mb-6">
          <div className="border-b border-gray-200">
            <nav className="flex space-x-8">
              {tabs.map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id)}
                  className={`py-3 px-1 border-b-2 font-medium text-sm transition-colors ${
                    activeTab === tab.id
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  {tab.label}
                  {tab.count > 0 && (
                    <span className={`ml-2 px-2 py-1 text-xs rounded-full ${
                      activeTab === tab.id ? 'bg-blue-100 text-blue-600' : 'bg-gray-100 text-gray-600'
                    }`}>
                      {tab.count}
                    </span>
                  )}
                </button>
              ))}
            </nav>
          </div>
        </div>

        {/* Mobile Search */}
        <div className="relative md:hidden mb-6">
          <Search className="w-5 h-5 text-gray-400 absolute left-3 top-1/2 transform -translate-y-1/2" />
          <input
            type="text"
            placeholder="Search accounts..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent w-full"
          />
        </div>

        {/* Account List */}
        <div className="space-y-4">
          {loading ? (
            <p>Loading accounts...</p>
          ) : error ? (
            <p className="text-red-500">{error}</p>
          ) : filteredAccounts.length > 0 ? (
            filteredAccounts.map((account) => (
              <AccountCard key={account.id} account={account} onViewDetails={() => setSelectedAccount(account)} />
            ))
          ) : (
            <div className="text-center py-12">
              <Wallet className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">No accounts found</h3>
              <p className="text-gray-600 mb-4">
                {searchQuery
                  ? 'Try adjusting your search terms or create a new account.'
                  : `No ${activeTab === 'all' ? '' : activeTab} accounts available. Create your first account to get started.`}
              </p>
              <button
                onClick={() => setIsCreateModalOpen(true)}
                className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors inline-flex items-center"
              >
                <Plus className="w-4 h-4 mr-2" />
                Open New Account
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Create Account Modal */}
      <CreateAccountModal
        isOpen={isCreateModalOpen}
        onClose={() => {
          setIsCreateModalOpen(false);
          setAccountCreated(prev => !prev); // Trigger refresh on close
        }}
      />

      {/* Account Details Modal */}
      {selectedAccount && (
        <AccountDetailsModal
          account={selectedAccount}
          onClose={() => setSelectedAccount(null)}
        />
      )}
    </div>
  );
}

export default Accounts;
