import React from "react";
import { X } from "lucide-react";

const AccountDetailsModal = ({ account, onClose }) => {
  if (!account) return null; // don't render if no account selected

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
      <div className="bg-white rounded-2xl shadow-lg p-6 w-full max-w-md relative">
        {/* Close button */}
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-gray-500 hover:text-gray-800"
        >
          <X className="w-5 h-5" />
        </button>

        <h2 className="text-xl font-semibold text-gray-800 mb-4">
          Account Details
        </h2>

        <div className="space-y-2 text-gray-700">
          {/* <p>
            <span className="font-medium">Name:</span> {account.name}
          </p> */}
          <p>
            <span className="font-medium">Account Number:</span>{" "}
            {account.accountNumber}
          </p>
          <p>
            <span className="font-medium">Branch:</span> {account.branchName}
          </p>
          <p>
            <span className="font-medium">IFSC Code:</span> {account.ifscCode}
          </p>
          <p>
            <span className="font-medium">Status:</span>{" "}
            {account.status.charAt(0).toUpperCase() + account.status.slice(1)}
          </p>
          <p>
            <span className="font-medium">Balance:</span>{" "}
            {new Intl.NumberFormat("en-IN", {
              style: "currency",
              currency: "INR",
              minimumFractionDigits: 2,
            }).format(account.balance)}
          </p>
        </div>

        <div className="mt-6 text-right">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default AccountDetailsModal;
