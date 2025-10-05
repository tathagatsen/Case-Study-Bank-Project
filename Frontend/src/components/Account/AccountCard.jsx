import React from "react";
import { Eye, Wallet, TrendingUp, CreditCard } from "lucide-react";

const AccountCard = ({ account, onViewDetails }) => {
  const getAccountIcon = (type) => {
    switch (type) {
      case "salary":
        return <Wallet className="w-8 h-8 text-blue-600" />;
      case "savings":
        return <TrendingUp className="w-8 h-8 text-green-600" />;
      case "current":
        return <CreditCard className="w-8 h-8 text-purple-600" />;
      default:
        return <Wallet className="w-8 h-8 text-gray-600" />;
    }
  };

  const getAccountTypeLabel = (type="") => {
    return type.charAt(0).toUpperCase() + type.slice(1) + " Account";
  };

  const formatBalance = (balance) => {
    return new Intl.NumberFormat("en-IN", {
      style: "currency",
      currency: "INR",
      minimumFractionDigits: 2,
    }).format(balance);
  };

  return (
    <div className="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-lg transition-shadow duration-200">
      <div className="flex items-start justify-between">
        <div className="flex items-start space-x-4">
          <div className="flex-shrink-0">{getAccountIcon(account.type)}</div>
          <div className="flex-grow">
            <h3 className="text-lg font-semibold text-gray-900 mb-1">
              
              {account.accountNumber}
            </h3>
            <p className="text-sm text-gray-600 mb-2">
              {getAccountTypeLabel(account.accountType)}
            </p>
            <p className="text-sm text-blue-600 font-medium mb-3">
              Branch: {account.branchName}
            </p>
            <p className="text-sm text-blue-600 font-medium mb-3">
              IFSC Code: {account.ifscCode}
            </p>
            <div className="flex items-center space-x-2">
              <span
                className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${
                  account.status.toLowerCase() === "active"
                    ? "bg-green-100 text-green-800"
                    : account.status === "inactive"
                    ? "bg-yellow-100 text-yellow-800"
                    : "bg-red-100 text-red-800"
                }`}
              >
                {account.status.charAt(0).toUpperCase() +
                  account.status.slice(1)}
              </span>
            </div>
          </div>
        </div>
        <div className="text-right">
          <p className="text-2xl font-bold text-green-600 mb-1">
            {formatBalance(account.balance)}
          </p>
          <p className="text-sm text-gray-500 mb-3">Available Balance</p>
          <button
            onClick={() => onViewDetails(account)}
            className="inline-flex items-center px-3 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200 transition-colors duration-200"
          >
            <Eye className="w-4 h-4 mr-2" />
            View Details
          </button>
        </div>
      </div>
    </div>
  );
};

export default AccountCard;
