import React from "react";
import { X } from "lucide-react";

const formatCurrency = (amount) =>
  new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "INR",
  }).format(amount);

const formatDate = (date) =>
  new Date(date).toLocaleDateString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });

export default function TransactionDetailsModal({ transaction, onClose }) {
  if (!transaction) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-lg w-full max-w-md p-6 relative">
        {/* Close button */}
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
        >
          <X className="w-5 h-5" />
        </button>

        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          Transaction Details
        </h2>

        <div className="space-y-3">
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">Description:</span>
            <span className="font-medium">{transaction.description}</span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">Recipient:</span>
            <span className="font-medium">{transaction.recipient}</span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">Account Number:</span>
            <span className="font-medium">{transaction.accountNumber}</span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">Amount:</span>
            <span
              className={`font-semibold ${
                transaction.type === "credit"
                  ? "text-green-600"
                  : "text-red-600"
              }`}
            >
              {transaction.type === "credit" ? "+" : "-"}
              {formatCurrency(transaction.amount)}
            </span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">Date:</span>
            <span className="font-medium">{formatDate(transaction.date)}</span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">Type:</span>
            <span className="font-medium capitalize">{transaction.type}</span>
          </div>
        </div>
      </div>
    </div>
  );
}
