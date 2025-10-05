import { useState } from "react";

const generateAccountNumber = () => {
  return "PQR" + Math.random().toString().slice(2, 15);
};

const mockAccounts = [
  {
    id: "1",
    accountNumber: "PQR1756982818852",
    type: "salary",
    balance: 45000,
    branch: "Chennai",
    status: "active",
    createdAt: new Date("2023-01-15"),
    accountHolderName: "John Doe",
  },
  {
    id: "2",
    accountNumber: "PQR1757047384604",
    type: "savings",
    balance: 125000,
    branch: "Delhi",
    status: "active",
    createdAt: new Date("2023-03-20"),
    accountHolderName: "Jane Smith",
  },
  {
    id: "3",
    accountNumber: "PQR1757123456789",
    type: "current",
    balance: 75000,
    branch: "Mumbai",
    status: "active",
    createdAt: new Date("2023-05-10"),
    accountHolderName: "Mike Johnson",
  },
  {
    id: "4",
    accountNumber: "PQR1757987654321",
    type: "savings",
    balance: 32000,
    branch: "Bangalore",
    status: "active",
    createdAt: new Date("2023-07-25"),
    accountHolderName: "Sarah Wilson",
  },
  {
    id: "5",
    accountNumber: "PQR1758123987456",
    type: "salary",
    balance: 58000,
    branch: "Hyderabad",
    status: "active",
    createdAt: new Date("2023-09-12"),
    accountHolderName: "David Brown",
  },
];

export const useAccounts = () => {
  const [accounts, setAccounts] = useState(mockAccounts);

  const createAccount = (accountData) => {
    const newAccount = {
      id: Date.now().toString(),
      accountNumber: generateAccountNumber(),
      type: accountData.type,
      balance: accountData.initialBalance,
      branch: accountData.branch,
      status: "active",
      createdAt: new Date(),
      accountHolderName: accountData.accountHolderName,
    };

    setAccounts((prev) => [...prev, newAccount]);
  };

  const getTotalBalance = () => {
    return accounts.reduce((total, account) => total + account.balance, 0);
  };

  const getAccountsByType = (type) => {
    if (type === "all") return accounts;
    return accounts.filter((account) => account.type === type);
  };

  const getAccountStats = () => {
    const total = accounts.length;
    const active = accounts.filter((acc) => acc.status === "active").length;
    const byType = {
      salary: accounts.filter((acc) => acc.type === "salary").length,
      savings: accounts.filter((acc) => acc.type === "savings").length,
      current: accounts.filter((acc) => acc.type === "current").length,
    };

    return { total, active, byType };
  };

  return {
    accounts,
    createAccount,
    getTotalBalance,
    getAccountsByType,
    getAccountStats,
  };
};
