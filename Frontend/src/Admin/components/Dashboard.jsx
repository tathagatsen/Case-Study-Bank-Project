import React, { useState, useEffect } from 'react';
import { 
  Users, 
  CreditCard, 
  Activity, 
  AlertTriangle, 
  TrendingUp, 
  Clock,
  Shield
} from 'lucide-react';
import { useBankingAdmin } from '../context/BankingAdminContext';
import LiveMetricsCard from './ui/LiveMetricsCard';
import ActivityFeed from './ui/ActivityFeed';

const Dashboard = () => {
  const { users, accounts, transactions } = useBankingAdmin();
  const [liveMetrics, setLiveMetrics] = useState({
    totalTransactions: 0,
    totalAmount: 0,
    activeUsers: 0,
    flaggedTransactions: 0
  });

  useEffect(() => {
    const interval = setInterval(() => {
      setLiveMetrics({
        totalTransactions: transactions.length + Math.floor(Math.random() * 5),
        totalAmount: transactions.reduce((sum, t) => sum + t.amount, 0) + (Math.random() * 1000),
        activeUsers: users.filter(u => u.status === 'active').length + Math.floor(Math.random() * 3),
        flaggedTransactions: transactions.filter(t => t.flagged).length + Math.floor(Math.random() * 2)
      });
    }, 5000);

    return () => clearInterval(interval);
  }, [transactions, users]);

  const recentActivity = [
    {
      id: '1',
      action: 'Account Frozen',
      user: 'John Doe',
      time: '2 minutes ago',
      severity: 'high'
    },
    {
      id: '2',
      action: 'Large Transaction Flagged',
      user: 'System',
      time: '5 minutes ago',
      severity: 'medium'
    },
    {
      id: '3',
      action: 'New User Registered',
      user: 'Sarah Wilson',
      time: '8 minutes ago',
      severity: 'low'
    }
  ];

  const dashboardCards = [
    {
      title: 'Total Users',
      value: users.length.toString(),
      change: '+12%',
      changeType: 'positive',
      icon: Users,
      color: 'blue'
    },
    {
      title: 'Active Accounts',
      value: accounts.filter(a => a.status === 'active').length.toString(),
      change: '+8%',
      changeType: 'positive',
      icon: CreditCard,
      color: 'green'
    },
    {
      title: 'Daily Transactions',
      value: liveMetrics.totalTransactions.toString(),
      change: '+24%',
      changeType: 'positive',
      icon: Activity,
      color: 'purple'
    },
    {
      title: 'Flagged Transactions',
      value: liveMetrics.flaggedTransactions.toString(),
      change: '-5%',
      changeType: 'negative',
      icon: AlertTriangle,
      color: 'red'
    }
  ];

  return (
    <div className="p-6">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
        <p className="text-gray-600 mt-1">Monitor and manage banking operations</p>
      </div>

      {/* Key Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {dashboardCards.map((card) => (
          <div key={card.title} className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">{card.title}</p>
                <p className="text-2xl font-bold text-gray-900 mt-1">{card.value}</p>
                <p className={`text-sm mt-1 ${
                  card.changeType === 'positive' ? 'text-green-600' : 'text-red-600'
                }`}>
                  {card.change} from last month
                </p>
              </div>
              <div className={`p-3 rounded-lg ${
                card.color === 'blue' ? 'bg-blue-100' :
                card.color === 'green' ? 'bg-green-100' :
                card.color === 'purple' ? 'bg-purple-100' :
                'bg-red-100'
              }`}>
                <card.icon className={`h-6 w-6 ${
                  card.color === 'blue' ? 'text-blue-600' :
                  card.color === 'green' ? 'text-green-600' :
                  card.color === 'purple' ? 'text-purple-600' :
                  'text-red-600'
                }`} />
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Live Metrics and Activity */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        <LiveMetricsCard 
          title="Real-time Transaction Volume"
          value={`$${liveMetrics.totalAmount.toLocaleString()}`}
          subtitle="Last 24 hours"
        />
        <ActivityFeed activities={recentActivity} />
      </div>

      {/* Critical Alerts */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <div className="flex items-center mb-4">
          <AlertTriangle className="h-5 w-5 text-red-500 mr-2" />
          <h2 className="text-lg font-semibold text-gray-900">Critical Alerts</h2>
        </div>
        <div className="space-y-3">
          <div className="flex items-center justify-between p-3 bg-red-50 rounded-lg border-l-4 border-red-500">
            <div className="flex items-center">
              <Shield className="h-4 w-4 text-red-500 mr-2" />
              <span className="text-sm text-red-800">Potential fraud detected on account #1001234568</span>
            </div>
            <span className="text-xs text-red-600">3 min ago</span>
          </div>
          <div className="flex items-center justify-between p-3 bg-amber-50 rounded-lg border-l-4 border-amber-500">
            <div className="flex items-center">
              <Clock className="h-4 w-4 text-amber-500 mr-2" />
              <span className="text-sm text-amber-800">Support ticket SLA breached - Ticket #tick1</span>
            </div>
            <span className="text-xs text-amber-600">15 min ago</span>
          </div>
          <div className="flex items-center justify-between p-3 bg-blue-50 rounded-lg border-l-4 border-blue-500">
            <div className="flex items-center">
              <TrendingUp className="h-4 w-4 text-blue-500 mr-2" />
              <span className="text-sm text-blue-800">System performance improved by 15%</span>
            </div>
            <span className="text-xs text-blue-600">1 hour ago</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
