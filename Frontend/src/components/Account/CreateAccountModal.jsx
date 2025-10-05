import React, { useEffect, useState } from "react";
import { X, Plus } from "lucide-react";
import { getAllBranches } from "../../api/AccountsApi";
import { createAccount } from "../../api/customerApi";
import PinModal from "./pinModal";
import { useSelector } from "react-redux";

const CreateAccountModal = ({ isOpen, onClose }) => {
  const [branches, setBranches] = useState([]);
  const [formData, setFormData] = useState({
    accountType: "savings",
    branchId: 0,
    accountHolderName: "",
    initialBalance: 0,
  });
  const [isPinModalOpen, setPinModalOpen] = useState(false);
  const [pendingAccountData, setPendingAccountData] = useState();
  const [isCreating, setIsCreating] = useState(false);
  const [errors, setErrors] = useState({});
  const user = useSelector((state) => state.user);
  useEffect(() => {
    getAllBranches()
      .then((res) => {
        console.log("Branches fetched:", res.data);
        setBranches(res.data);
      })
      .catch((err) => {
        console.error("Error fetching branches:", err);
      });
  }, []);

  const validateForm = () => {
    const newErrors = {};

    if (!formData.accountHolderName.trim()) {
      newErrors.accountHolderName = "Account holder name is required";
    }

    if (!formData.branchId) {
      newErrors.branchId = "Branch selection is required";
    }

    if (formData.initialBalance < 0) {
      newErrors.initialBalance = "Initial balance must be positive";
    }

    if (formData.accountType === "savings" && formData.initialBalance < 500) {
      newErrors.initialBalance = "Minimum balance for savings account is ₹500";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      setPendingAccountData(formData); // Save form data for the pin modal
      setPinModalOpen(true); // Show pin modal
    }
  };

  const handleInputChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: undefined }));
    }
  };

  const finalSubmit = (finalData) => {

    setIsCreating(true);
    console.log("Final data to submit:", { ...finalData, customerId: user.userId });
    // Call the create account API
    // setIsCreating(false);
    
    createAccount({ ...finalData, customerId: user.userId })
    .then((res) => {
      console.log("Account created:", res.data);
      setIsCreating(false);
      setPinModalOpen(false);
      onClose(); // Close the create account modal
    })
    .catch((err) => {
      console.error("Error creating account:", err);
      setIsCreating(false);
      // Handle error (e.g., show toast)
    }
    );
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg max-w-md w-full p-6">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-semibold text-gray-900">
            Open New Account
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <X className="w-6 h-6" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Account Type
            </label>
            <select
              value={formData.accountType}
              onChange={(e) => handleInputChange("accountType", e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="SAVINGS">Savings Account</option>
              <option value="SALARY">Salary Account</option>
              <option value="CURRENT">Current Account</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Account Holder Name
            </label>
            <input
              type="text"
              value={formData.accountHolderName}
              onChange={(e) =>
                handleInputChange("accountHolderName", e.target.value)
              }
              className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent ${
                errors.accountHolderName ? "border-red-300" : "border-gray-300"
              }`}
              placeholder="Enter full name"
            />
            {errors.accountHolderName && (
              <p className="text-red-500 text-xs mt-1">
                {errors.accountHolderName}
              </p>
            )}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Branch
            </label>
            <select
              value={formData.branchId}
              onChange={(e) => handleInputChange("branchId", Number(e.target.value) || 0)}
              className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent ${
                errors.branchId ? "border-red-300" : "border-gray-300"
              }`}
            >
              <option value="0">Select Branch</option>
              {branches.map((branch) => (
                <option key={branch.branchId} value={branch.branchId}>
                  {branch.branchName}
                </option>
              ))}
            </select>
            {errors.branchId && (
              <p className="text-red-500 text-xs mt-1">{errors.branchId}</p>
            )}
          </div>

          {/* <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Initial Balance (₹)
            </label>
            <input
              type="number"
              value={formData.initialBalance}
              onChange={(e) =>
                handleInputChange(
                  "initialBalance",
                  parseFloat(e.target.value) || 0
                )
              }
              className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent ${
                errors.initialBalance ? "border-red-300" : "border-gray-300"
              }`}
              placeholder="0.00"
              min="0"
              step="0.01"
            />
            {errors.initialBalance && (
              <p className="text-red-500 text-xs mt-1">
                {errors.initialBalance}
              </p>
            )}
            <p className="text-gray-500 text-xs mt-1">
              {formData.accountType === "savings" && "Minimum balance: ₹500"}
              {formData.accountType === "current" &&
                "No minimum balance required"}
              {formData.accountType === "salary" &&
                "No minimum balance required"}
            </p>
          </div> */}

          <div className="flex space-x-3 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-4 py-2 text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors flex items-center justify-center"
            >
              <Plus className="w-4 h-4 mr-2" />
              Create Account
            </button>
          </div>
        </form>
      </div>
      {isPinModalOpen && (
      <PinModal
        accountData={pendingAccountData}
        isOpen={true}
        onClose={() => setPinModalOpen(false)}
        isLoading={isCreating}
        onSubmit={finalSubmit}
      />
    )}
    </div>
  );
};

export default CreateAccountModal;
