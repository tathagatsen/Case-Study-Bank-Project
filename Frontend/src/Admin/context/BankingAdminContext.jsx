import React, { createContext, useContext, useState, useEffect } from 'react';
import { generateMockData } from '../utils/mockData';
import { listAllCustomers } from '../../api/customerApi';
import { deactivateCustomer } from '../../api/AdminApi';
import { toast } from 'react-hot-toast';
import { getAllAccounts } from '../../api/AccountsApi';
import { getAllTransaction} from '../../api/Transaction';
import { getAllTickets } from '../../api/customerApi';

// Context without TypeScript types
const BankingAdminContext = createContext(undefined);

export const BankingAdminProvider = ({ children }) => {
  const [users, setUsers] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [accounts, setAccounts] = useState([]);
  const [tickets, setTickets] = useState([]);
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCustomers();
     fetchAccounts();
     fetchTransactions();
     fetchRecentActivities();
     fetchTickets();
    const data = generateMockData();

    console.log(accounts);
    
    
  
 
    
    setLoading(false);
  }, []);


  const fetchTickets = async () => {
  try {
    setLoading(true);
    const res = await getAllTickets();
    // Map your DB columns to frontend-friendly keys
    const mappedTickets = res.data.map(t => ({
      id: t.id, // TICKET_ID
      createdAt: t.createdAt ? new Date(t.createdAt) : null, // handle date
      description: t.description,
      status: t.status,
      subject: t.subject,
      customerId: t.customerId,
      email: t.email,
    }));

    console.log(mappedTickets)
    setTickets(mappedTickets);
  } catch (err) {
    console.error("Error fetching tickets", err);
  } finally {
    setLoading(false);
  }
};


  const fetchCustomers = async () => {
  try {
    setLoading(true);
    const res = await listAllCustomers();
    setUsers(res.data);
    console.log("Fetched customers:", res.data);
  } catch (err) {
    console.error("Error fetching customers", err);
  } finally {
    setLoading(false);
  }
};


  const refreshData = () => {
    setLoading(true);
    setTimeout(() => {
      const data = generateMockData();
      setUsers(data.users);
      setTransactions(data.transactions);
      setAccounts(data.accounts);
      setTickets(data.tickets);
      setLogs(data.logs);
      setLoading(false);
    }, 1000);
  };

  const updateUser = (id, updates) => {
    setUsers(prev => prev.map(user => user.id === id ? { ...user, ...updates } : user));
  };

  const updateAccount = (id, updates) => {
    setAccounts(prev => prev.map(account => account.id === id ? { ...account, ...updates } : account));
  };

  const reverseTransaction = (id) => {
    setTransactions(prev => prev.map(transaction => 
      transaction.id === id 
        ? { ...transaction, status: 'reversed', reversedAt: new Date() }
        : transaction
    ));
  };

  const flagTransaction = (id, reason) => {
    setTransactions(prev => prev.map(transaction =>
      transaction.id === id
        ? { ...transaction, flagged: true, flagReason: reason }
        : transaction
    ));
  };

  const updateTicket = (id, updates) => {
    setTickets(prev => prev.map(ticket => ticket.id === id ? { ...ticket, ...updates } : ticket));
  };

  const toggleUserStatus = async (user) => {
  try {
    console.log("Toggling status for user:", user);
    if (user.active === true) {
      console.log(user.active);
      await deactivateCustomer(user.id); // Calls backend API

      setUsers(prev => prev.map(u => u.id === user.id ? { ...u, active: false } : u));
      toast.success(`Deactivated ${user.firstName}`);
    } else {
      toast.error("Reactivation not implemented");
    }

  

  } catch (error) {
    toast.error("Failed to toggle status");
  }
};

// In BankingAdminProvider, after fetching accounts:
const fetchAccounts = async () => {
  try {
    setLoading(true);
    const res = await getAllAccounts();
    // Map backend DB columns to expected frontend keys
    const mappedAccounts = res.data.map(a => ({
      id: a.accountId, // from ACCOUNT_ID
      accountNumber: a.accountNumber,
      userId: a.customerId,    // change this if you want to show email or something else!
      accountType: a.accountType,
      balance: a.balance,
      status: a.status,
      createdAt: new Date(a.createdAt),    // parse to Date object if string
      lastActivity: new Date(a.updatedAt),
      // add others if needed
      email: a.email
    }));
    setAccounts(mappedAccounts);
  } catch (err) {
    console.error("Error fetching accounts", err);
  } finally {
    setLoading(false);
  }
};


const fetchTransactions = async () => {
  try {
    setLoading(true);
    const res = await getAllTransaction();
    const mappedTransactions = res.data.map(t => ({
      id: t.id,
      reference: t.id.toString(), // or customize, if you have a better "reference"
      amount: t.amount,
      createdAt: new Date(t.createdAt),
      description: t.description,
      flagReason: t.flagReason,
      flagged: t.flagged === 1, // DB 1/0 -> JS true/false
      flaggedAt: t.flaggedAt ? new Date(t.flaggedAt) : null,
      flaggedBy: t.flaggedBy,
      fromAccountNumber: t.fromAccountNumber,
      toAccountNumber: t.toAccountNumber,
      status: t.status,
      type: t.type,
      // For your table's "accountId" column, decide what to show:
      accountId: t.fromAccountNumber, // Or t.toAccountNumber, or combine them, as you prefer
    }));
    setTransactions(mappedTransactions);
  } catch (err) {
    console.error("Error fetching transactions", err);
  } finally {
    setLoading(false);
  }
};

const fetchRecentActivities = async () => {
  try {
    setLoading(true);
    const res = await getRecentActivities();
    // Map Oracle fields to JS-friendly properties if backend doesn't do so
    const mappedLogs = res.data.map(log => ({
      id: log.id,
      actionType: log.actionType,
      adminId: log.adminId,
      details: log.details,
      targetId: log.targetId,
      targetType: log.targetType,
      timestamp: log.timestamp ? new Date(log.timestamp) : null, // parse to Date
    }));
    setLogs(mappedLogs);
  } catch (err) {
    console.error("Error fetching activity logs", err);
  } finally {
    setLoading(false);
  }
};


  const value = {
    users,
    transactions,
    accounts,
    tickets,
    logs,
    loading,
    refreshData,
    updateUser,
    updateAccount,
    reverseTransaction,
    flagTransaction,
    updateTicket,
    toggleUserStatus
  };

  return (
    <BankingAdminContext.Provider value={value}>
      {children}
    </BankingAdminContext.Provider>
  );
};

export const useBankingAdmin = () => {
  const context = useContext(BankingAdminContext);
  if (!context) {
    throw new Error('useBankingAdmin must be used within a BankingAdminProvider');
  }
  return context;
};
