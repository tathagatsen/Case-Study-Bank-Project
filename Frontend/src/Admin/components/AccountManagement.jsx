import React, { useState } from "react";
import { Search, Ban, Unlock, Eye, AlertTriangle, Snowflake } from "lucide-react";
import { useBankingAdmin } from "../context/BankingAdminContext";
import Modal from "./ui/Modal";
import Button from "./ui/Button";

const AccountManagement = () => {
  const { accounts, loading, updateAccount } = useBankingAdmin();
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("all");
  const [selectedAccount, setSelectedAccount] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [actionModalOpen, setActionModalOpen] = useState(false);
  const [actionType, setActionType] = useState("freeze");
  const [actionReason, setActionReason] = useState("");

  const filteredAccounts = accounts.filter((account) => {
    const matchesSearch =
      account.accountNumber.includes(searchTerm) ||
      account.userId.toLowerCase().includes(searchTerm.toLowerCase()) ||
      account.accountType.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus =
      statusFilter === "all" || account.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const handleViewAccount = (account) => {
    setSelectedAccount(account);
    setIsModalOpen(true);
  };

  const handleAccountAction = (account, type) => {
    setSelectedAccount(account);
    setActionType(type);
    setActionModalOpen(true);
  };

  const submitAccountAction = () => {
    if (selectedAccount && actionReason) {
      const updates = {};

      switch (actionType) {
        case "freeze":
          updates.status = "frozen";
          updates.freezeReason = actionReason;
          break;
        case "blacklist":
          updates.status = "blacklisted";
          updates.blacklistReason = actionReason;
          break;
        case "activate":
          updates.status = "active";
          updates.freezeReason = undefined;
          updates.blacklistReason = undefined;
          break;
        default:
          break;
      }

      updateAccount(selectedAccount.id, updates);
      setActionModalOpen(false);
      setActionReason("");
      setSelectedAccount(null);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "active":
        return "bg-green-100 text-green-800";
      case "frozen":
        return "bg-blue-100 text-blue-800";
      case "blacklisted":
        return "bg-red-100 text-red-800";
      case "closed":
        return "bg-gray-100 text-gray-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  if (loading) {
    return (
      <div className="p-6">
        <div className="animate-pulse space-y-4">
          <div className="h-8 bg-gray-300 rounded w-1/4"></div>
          <div className="h-96 bg-gray-300 rounded"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="p-6">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Account Management</h1>
        <p className="text-gray-600 mt-1">
          Freeze, blacklist, and manage customer accounts
        </p>
      </div>

      {/* Controls */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-6">
        <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
            <input
              type="text"
              placeholder="Search by account number, user ID, or type..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 pr-4 py-2 w-full border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="all">All Status</option>
            <option value="active">Active</option>
            <option value="frozen">Frozen</option>
            <option value="blacklisted">Blacklisted</option>
            <option value="closed">Closed</option>
          </select>
        </div>
      </div>

      {/* Accounts Table */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Account
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  User ID
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Type
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Balance
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Last Activity
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredAccounts.map((account) => (
                <tr
                  key={account.id}
                  className="hover:bg-gray-50 transition-colors"
                >
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <span className="text-sm font-medium text-gray-900">
                        {account.accountNumber}
                      </span>
                      {(account.status === "frozen" ||
                        account.status === "blacklisted") && (
                        <AlertTriangle className="h-4 w-4 text-red-500 ml-2" />
                      )}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {account.userId}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {account.accountType}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ${account.balance.toLocaleString()}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span
                      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(
                        account.status
                      )}`}
                    >
                      {account.status}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {account.lastActivity.toLocaleDateString()}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                    <button
                      onClick={() => handleViewAccount(account)}
                      className="text-blue-600 hover:text-blue-900 transition-colors"
                    >
                      <Eye className="h-4 w-4" />
                    </button>
                    {account.status === "active" && (
                      <>
                        <button
                          onClick={() =>
                            handleAccountAction(account, "freeze")
                          }
                          className="text-blue-600 hover:text-blue-900 transition-colors"
                        >
                          <Snowflake className="h-4 w-4" />
                        </button>
                        <button
                          onClick={() =>
                            handleAccountAction(account, "blacklist")
                          }
                          className="text-red-600 hover:text-red-900 transition-colors"
                        >
                          <Ban className="h-4 w-4" />
                        </button>
                      </>
                    )}
                    {(account.status === "frozen" ||
                      account.status === "blacklisted") && (
                      <button
                        onClick={() => handleAccountAction(account, "activate")}
                        className="text-green-600 hover:text-green-900 transition-colors"
                      >
                        <Unlock className="h-4 w-4" />
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Account Details Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Account Details"
      >
        {selectedAccount && (
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Account Number
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {selectedAccount.accountNumber}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  User ID
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {selectedAccount.userId}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Account Type
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {selectedAccount.accountType}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Balance
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  ${selectedAccount.balance.toLocaleString()}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Status
                </label>
                <span
                  className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(
                    selectedAccount.status
                  )}`}
                >
                  {selectedAccount.status}
                </span>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Created
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {selectedAccount.createdAt.toLocaleDateString()}
                </p>
              </div>
            </div>
            {selectedAccount.freezeReason && (
              <div className="bg-blue-50 p-4 rounded-lg">
                <label className="block text-sm font-medium text-blue-800">
                  Freeze Reason
                </label>
                <p className="mt-1 text-sm text-blue-700">
                  {selectedAccount.freezeReason}
                </p>
              </div>
            )}
            {selectedAccount.blacklistReason && (
              <div className="bg-red-50 p-4 rounded-lg">
                <label className="block text-sm font-medium text-red-800">
                  Blacklist Reason
                </label>
                <p className="mt-1 text-sm text-red-700">
                  {selectedAccount.blacklistReason}
                </p>
              </div>
            )}
          </div>
        )}
      </Modal>

      {/* Account Action Modal */}
      <Modal
        isOpen={actionModalOpen}
        onClose={() => setActionModalOpen(false)}
        title={`${actionType.charAt(0).toUpperCase() + actionType.slice(1)} Account`}
      >
        <div className="space-y-4">
          <div className="bg-amber-50 p-4 rounded-lg">
            <p className="text-sm text-amber-800">
              You are about to {actionType} account{" "}
              {selectedAccount?.accountNumber}.
              {actionType !== "activate" &&
                " This will restrict the customer's access to their funds."}
            </p>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              {actionType === "activate"
                ? "Activation Notes"
                : "Reason for " + actionType}
            </label>
            <textarea
              value={actionReason}
              onChange={(e) => setActionReason(e.target.value)}
              rows={4}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
              placeholder={`Enter the reason for ${actionType}ing this account...`}
            />
          </div>
          <div className="flex justify-end space-x-3">
            <Button
              variant="secondary"
              onClick={() => {
                setActionModalOpen(false);
                setActionReason("");
              }}
            >
              Cancel
            </Button>
            <Button
              onClick={submitAccountAction}
              className={
                actionType === "blacklist"
                  ? "bg-red-600 hover:bg-red-700"
                  : actionType === "freeze"
                  ? "bg-blue-600 hover:bg-blue-700"
                  : "bg-green-600 hover:bg-green-700"
              }
              disabled={!actionReason.trim()}
            >
              {actionType.charAt(0).toUpperCase() + actionType.slice(1)} Account
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default AccountManagement;
