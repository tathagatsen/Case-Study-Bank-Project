import { User, Transaction, Account, SupportTicket, AdminLog } from '../types';

export const generateMockData = () => {
  const users: User[] = [
    {
      id: '1',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@bank.com',
      role: 'admin',
      status: 'active',
      lastLogin: new Date(Date.now() - 1000 * 60 * 30),
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 365),
      permissions: ['user:read', 'user:write', 'transaction:read', 'transaction:write', 'system:admin']
    },
    {
      id: '2',
      firstName: 'Sarah',
      lastName: 'Wilson',
      email: 'sarah.wilson@bank.com',
      role: 'manager',
      status: 'active',
      lastLogin: new Date(Date.now() - 1000 * 60 * 60 * 2),
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 180),
      permissions: ['user:read', 'transaction:read', 'account:manage']
    },
    {
      id: '3',
      firstName: 'Mike',
      lastName: 'Johnson',
      email: 'mike.johnson@bank.com',
      role: 'analyst',
      status: 'active',
      lastLogin: new Date(Date.now() - 1000 * 60 * 60 * 8),
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 90),
      permissions: ['transaction:read', 'reports:generate']
    },
    {
      id: '4',
      firstName: 'Emma',
      lastName: 'Brown',
      email: 'emma.brown@bank.com',
      role: 'support',
      status: 'inactive',
      lastLogin: new Date(Date.now() - 1000 * 60 * 60 * 24 * 7),
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 30),
      permissions: ['tickets:manage', 'user:read']
    }
  ];

  const accounts: Account[] = [
    {
      id: 'acc1',
      accountNumber: '1001234567',
      userId: 'user1',
      accountType: 'Checking',
      balance: 15420.50,
      status: 'active',
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 365),
      lastActivity: new Date(Date.now() - 1000 * 60 * 60 * 2)
    },
    {
      id: 'acc2',
      accountNumber: '1001234568',
      userId: 'user2',
      accountType: 'Savings',
      balance: 75000.00,
      status: 'frozen',
      freezeReason: 'Suspicious activity detected',
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 200),
      lastActivity: new Date(Date.now() - 1000 * 60 * 60 * 24 * 3)
    },
    {
      id: 'acc3',
      accountNumber: '1001234569',
      userId: 'user3',
      accountType: 'Checking',
      balance: 0.00,
      status: 'blacklisted',
      blacklistReason: 'Fraud confirmed',
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 100),
      lastActivity: new Date(Date.now() - 1000 * 60 * 60 * 24 * 30)
    }
  ];

  const transactions: Transaction[] = [
    {
      id: 'txn1',
      accountId: 'acc1',
      type: 'debit',
      amount: 250.00,
      description: 'ATM Withdrawal',
      status: 'completed',
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 2),
      flagged: false,
      reference: 'TXN20241201001'
    },
    {
      id: 'txn2',
      accountId: 'acc2',
      type: 'credit',
      amount: 5000.00,
      description: 'Large Cash Deposit',
      status: 'completed',
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24),
      flagged: true,
      flagReason: 'Large amount exceeds threshold',
      reference: 'TXN20241201002'
    },
    {
      id: 'txn3',
      accountId: 'acc1',
      type: 'transfer',
      amount: 1500.00,
      description: 'Wire Transfer',
      status: 'pending',
      createdAt: new Date(Date.now() - 1000 * 60 * 30),
      flagged: false,
      recipientAccountId: 'acc2',
      reference: 'TXN20241201003'
    },
    {
      id: 'txn4',
      accountId: 'acc3',
      type: 'debit',
      amount: 10000.00,
      description: 'Suspicious Transfer',
      status: 'reversed',
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 2),
      reversedAt: new Date(Date.now() - 1000 * 60 * 60 * 23),
      flagged: true,
      flagReason: 'Fraud detected',
      reference: 'TXN20241201004'
    }
  ];

  const tickets: SupportTicket[] = [
    {
      id: 'tick1',
      userId: 'user1',
      subject: 'Account Access Issues',
      description: 'Customer unable to login for the past 2 days',
      status: 'open',
      priority: 'high',
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 6),
      updatedAt: new Date(Date.now() - 1000 * 60 * 60 * 2),
      slaBreached: true
    },
    {
      id: 'tick2',
      userId: 'user2',
      subject: 'Fraudulent Transaction Report',
      description: 'Customer reports unauthorized transaction on account',
      status: 'escalated',
      priority: 'critical',
      assignedTo: '2',
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 12),
      updatedAt: new Date(Date.now() - 1000 * 60 * 60 * 1),
      escalatedAt: new Date(Date.now() - 1000 * 60 * 60 * 8),
      slaBreached: false
    },
    {
      id: 'tick3',
      userId: 'user3',
      subject: 'Card Replacement Request',
      description: 'Lost card needs replacement',
      status: 'resolved',
      priority: 'medium',
      assignedTo: '4',
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 2),
      updatedAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 1),
      resolvedAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 1),
      slaBreached: false
    }
  ];

  const logs: AdminLog[] = [
    {
      id: 'log1',
      adminId: '1',
      action: 'ACCOUNT_FROZEN',
      resource: 'Account',
      resourceId: 'acc2',
      details: 'Account frozen due to suspicious activity',
      timestamp: new Date(Date.now() - 1000 * 60 * 60 * 3),
      ipAddress: '192.168.1.100'
    },
    {
      id: 'log2',
      adminId: '2',
      action: 'TRANSACTION_FLAGGED',
      resource: 'Transaction',
      resourceId: 'txn2',
      details: 'Transaction flagged for manual review',
      timestamp: new Date(Date.now() - 1000 * 60 * 60 * 6),
      ipAddress: '192.168.1.101'
    },
    {
      id: 'log3',
      adminId: '1',
      action: 'USER_ROLE_UPDATED',
      resource: 'User',
      resourceId: '3',
      details: 'User role changed from support to analyst',
      timestamp: new Date(Date.now() - 1000 * 60 * 60 * 24),
      ipAddress: '192.168.1.100'
    }
  ];

  return { users, transactions, accounts, tickets, logs };
};