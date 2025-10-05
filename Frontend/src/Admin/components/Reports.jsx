import React, { useState } from 'react';
import { Download, Calendar, FileText, TrendingUp, Users, CreditCard, Activity } from 'lucide-react';
import Button from './ui/Button';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import { useBankingAdmin } from '../context/BankingAdminContext';

const Reports = () => {
  const { users, transactions, accounts, loading } = useBankingAdmin();

  const [selectedReportType, setSelectedReportType] = useState('accounts');
  const [dateRange, setDateRange] = useState({
    start: new Date(Date.now() - 1000 * 60 * 60 * 24 * 30).toISOString().split('T')[0],
    end: new Date().toISOString().split('T')[0]
  });

  const reportTypes = [
    {
      id: 'users',
      name: 'User Activity Report',
      description: 'User login patterns and behavior analysis',
      icon: Users,
      color: 'purple'
    },
    {
      id: 'accounts',
      name: 'Account Summary Report',
      description: 'Account balances, status, and activity summary',
      icon: CreditCard,
      color: 'blue'
    },
    {
      id: 'transactions',
      name: 'Transaction Report',
      description: 'Detailed transaction history and analytics',
      icon: Activity,
      color: 'green'
    },
    
    
  ];

  const handleGenerateReport = () => {
    const reportData = {
      type: selectedReportType,
      dateRange,
      timestamp: new Date().toISOString()
    };
    
    console.log('Generating report:', reportData);
    alert(`${reportTypes.find(t => t.id === selectedReportType)?.name} generated successfully!`);
  };

  const handleDownloadPDF = async () => {
    const input = document.getElementById('report-table');
    const canvas = await html2canvas(input);
    const imgData = canvas.toDataURL('image/png');
    const pdf = new jsPDF('l', 'mm', 'a4');
    const imgProps = pdf.getImageProperties(imgData);
    const pdfWidth = pdf.internal.pageSize.getWidth();
    const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;

    pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
    pdf.save(`${selectedReportType}-report.pdf`);
  };

  return (
    <div className="p-6">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Reports</h1>
        <p className="text-gray-600 mt-1">Generate and export detailed system reports</p>
      </div>

      {/* Report Type Selection */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-6">
       

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
          {reportTypes.map((type) => (
            <div
              key={type.id}
              onClick={() => setSelectedReportType(type.id)}
              className={`p-4 rounded-lg border-2 cursor-pointer transition-all hover:scale-105 ${
                selectedReportType === type.id 
                  ? 'border-blue-500 bg-blue-50' 
                  : 'border-gray-200 hover:border-gray-300'
              }`}
            >
              <div className="flex items-center mb-2">
                <type.icon className={`h-6 w-6 mr-2 ${
                  type.color === 'blue' ? 'text-blue-600' :
                  type.color === 'green' ? 'text-green-600' :
                  type.color === 'purple' ? 'text-purple-600' :
                  'text-red-600'
                }`} />
                <h3 className="font-medium text-gray-900">{type.name}</h3>
              </div>
              <p className="text-sm text-gray-600">{type.description}</p>
            </div>
          ))}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Start Date</label>
            <input
              type="date"
              value={dateRange.start}
              onChange={(e) => setDateRange(prev => ({ ...prev, start: e.target.value }))}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">End Date</label>
            <input
              type="date"
              value={dateRange.end}
              onChange={(e) => setDateRange(prev => ({ ...prev, end: e.target.value }))}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>
          <div className="flex items-end">
            {/* <Button
              onClick={handleGenerateReport}
              className="w-full bg-blue-600 hover:bg-blue-700"
            >
              <Download className="h-4 w-4 mr-2" />
              Generate Report
            </Button> */}
          </div>
        </div>
      </div>

      {/* Report Table + Export */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold text-gray-900">
            {reportTypes.find(t => t.id === selectedReportType)?.name}
          </h2>
          <Button onClick={handleDownloadPDF} className="bg-blue-600 hover:bg-red-700">
            <Download className="h-4 w-4 mr-2" />
            Export to PDF
          </Button>
        </div>

        <div id="report-table" className="overflow-x-auto">
          {loading ? (
            <p className="text-gray-500">Loading data...</p>
          ) : (
            <>
              {selectedReportType === 'accounts' && (
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2">Account Number</th>
                      <th className="px-4 py-2">Type</th>
                      <th className="px-4 py-2">Balance</th>
                      <th className="px-4 py-2">Status</th>
                      <th className="px-4 py-2">User ID</th>
                    </tr>
                  </thead>
                  <tbody>
                    {accounts.map(account => (
                      <tr key={account.id} className="hover:bg-gray-50">
                        <td className="px-4 py-2">{account.accountNumber}</td>
                        <td className="px-4 py-2">{account.accountType}</td>
                        <td className="px-4 py-2">₹{account.balance}</td>
                        <td className="px-4 py-2">{account.status}</td>
                        <td className="px-4 py-2">{account.userId}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}

              {selectedReportType === 'transactions' && (
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2">Reference</th>
                      <th className="px-4 py-2">Amount</th>
                      <th className="px-4 py-2">From</th>
                      <th className="px-4 py-2">To</th>
                      <th className="px-4 py-2">Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {transactions.map(txn => (
                      <tr key={txn.id} className="hover:bg-gray-50">
                        <td className="px-4 py-2">{txn.reference}</td>
                        <td className="px-4 py-2">₹{txn.amount}</td>
                        <td className="px-4 py-2">{txn.fromAccountNumber}</td>
                        <td className="px-4 py-2">{txn.toAccountNumber}</td>
                        <td className="px-4 py-2">{txn.status}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}

              {selectedReportType === 'users' && (
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2">Customer ID</th>
                      <th className="px-4 py-2">Name</th>
                      <th className="px-4 py-2">Email</th>
                      <th className="px-4 py-2">Active</th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map(user => (
                      <tr key={user.customerId} className="hover:bg-gray-50">
                        <td className="px-4 py-2">{user.customerId}</td>
                        <td className="px-4 py-2">{user.name}</td>
                        <td className="px-4 py-2">{user.email}</td>
                        <td className="px-4 py-2">{user.active ? 'Yes' : 'No'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}

              {/* {selectedReportType === 'fraud' && (
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2">Transaction ID</th>
                      <th className="px-4 py-2">Amount</th>
                      <th className="px-4 py-2">Flag Reason</th>
                      <th className="px-4 py-2">Flagged By</th>
                      <th className="px-4 py-2">Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    {transactions.filter(t => t.flagged).map(txn => (
                      <tr key={txn.id} className="hover:bg-gray-50">
                        <td className="px-4 py-2">{txn.id}</td>
                        <td className="px-4 py-2">₹{txn.amount}</td>
                        <td className="px-4 py-2">{txn.flagReason}</td>
                        <td className="px-4 py-2">{txn.flaggedBy}</td>
                        <td className="px-4 py-2">{txn.flaggedAt?.toLocaleString()}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )} */}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default Reports;
