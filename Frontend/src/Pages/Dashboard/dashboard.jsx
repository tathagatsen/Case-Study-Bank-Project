import React from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import {
  Users,
  CreditCard,
  ArrowLeftRight,
  Calendar,
  LifeBuoy,
  IdCard,
} from 'lucide-react';

const Dashboard = () => {
  const user = useSelector((state) => state.user);
  const navigate = useNavigate();

  const services = [
    {
      id: 'accounts',
      name: 'Accounts',
      description: 'View balances, account details, and recent activity.',
      icon: CreditCard,
      to: '/account',
      color: 'from-blue-500 to-indigo-600',
      iconColor: 'text-blue-600',
    },
    {
      id: 'transactions',
      name: 'Transaction',
      description: 'Transfer money and review your transaction history.',
      icon: ArrowLeftRight,
      to: '/transaction',
      color: 'from-emerald-500 to-teal-600',
      iconColor: 'text-emerald-600',
    },
    {
      id: 'deposit',
      name: 'Deposit',
      description: 'Deposit funds into your account instantly.',
      icon: Calendar, // or ArrowDownLeft if you prefer
      to: '/deposit',
      color: 'from-green-500 to-lime-600',
      iconColor: 'text-green-600',
    },
    {
      id: 'support',
      name: 'Help & Support',
      description: 'Open and manage support tickets.',
      icon: LifeBuoy,
      to: '/support',
      color: 'from-purple-500 to-fuchsia-600',
      iconColor: 'text-purple-600',
    },
    {
      id: 'kyc',
      name: 'KYC',
      description: 'Upload and manage your KYC documents.',
      icon: IdCard,
      to: '/kyc',
      color: 'from-orange-500 to-rose-600',
      iconColor: 'text-orange-600',
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50">
      <div className="container mx-auto px-4 py-8 max-w-7xl">
        {/* Welcome Banner */}
        <div className="mb-8">
          <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 md:p-8">
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 rounded-xl bg-gradient-to-r from-indigo-500 to-blue-600" />
              <div>
                <h1 className="text-2xl md:text-3xl font-bold text-gray-900">
                  Welcome{user?.name ? `, ${user.name}` : ''}!
                </h1>
                <p className="text-gray-600 mt-1">
                  Access your banking services quickly from the options below.
                </p>
              </div>
            </div>
          </div>
        </div>

        {/* Services Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {services.map((svc) => (
            <button
              key={svc.id}
              onClick={() => navigate(svc.to)}
              className="group text-left bg-white rounded-2xl shadow-sm border border-gray-100 p-6 hover:shadow-md transition-all focus:outline-none focus:ring-2 focus:ring-blue-500"
              aria-label={`Open ${svc.name}`}
            >
              <div className="flex items-start gap-4">
                <div className={`p-3 rounded-xl bg-gradient-to-br ${svc.color} bg-opacity-10`}>
                  <svc.icon className={`w-6 h-6 ${svc.iconColor}`} />
                </div>
                <div className="flex-1">
                  <h3 className="text-lg font-semibold text-gray-900 group-hover:text-blue-700">
                    {svc.name}
                  </h3>
                  <p className="text-sm text-gray-600 mt-1">{svc.description}</p>
                </div>
              </div>
              <div className="mt-4 h-1 w-0 group-hover:w-full bg-gradient-to-r from-blue-500 to-indigo-600 transition-all rounded" />
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;