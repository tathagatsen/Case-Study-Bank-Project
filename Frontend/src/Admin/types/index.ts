export type UserRole = 'admin' | 'manager' | 'analyst' | 'support';
export type UserStatus = 'active' | 'inactive' | 'suspended';
export type AccountStatus = 'active' | 'frozen' | 'blacklisted' | 'closed';
export type TransactionStatus = 'completed' | 'pending' | 'failed' | 'reversed';
export type TransactionType = 'debit' | 'credit' | 'transfer' | 'fee';
export type TicketStatus = 'open' | 'in-progress' | 'resolved' | 'escalated';
export type TicketPriority = 'low' | 'medium' | 'high' | 'critical';

export interface User {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  role: UserRole;
  status: UserStatus;
  lastLogin: Date;
  createdAt: Date;
  permissions: string[];
}

export interface Account {
  id: string;
  accountNumber: string;
  userId: string;
  accountType: string;
  balance: number;
  status: AccountStatus;
  freezeReason?: string;
  blacklistReason?: string;
  createdAt: Date;
  lastActivity: Date;
}

export interface Transaction {
  id: string;
  accountId: string;
  type: TransactionType;
  amount: number;
  description: string;
  status: TransactionStatus;
  createdAt: Date;
  reversedAt?: Date;
  flagged: boolean;
  flagReason?: string;
  recipientAccountId?: string;
  reference: string;
}

export interface SupportTicket {
  id: string;
  userId: string;
  subject: string;
  description: string;
  status: TicketStatus;
  priority: TicketPriority;
  assignedTo?: string;
  createdAt: Date;
  updatedAt: Date;
  escalatedAt?: Date;
  resolvedAt?: Date;
  slaBreached: boolean;
}

export interface AdminLog {
  id: string;
  adminId: string;
  action: string;
  resource: string;
  resourceId: string;
  details: string;
  timestamp: Date;
  ipAddress: string;
}

export interface SystemMetrics {
  totalUsers: number;
  activeAccounts: number;
  dailyTransactions: number;
  flaggedTransactions: number;
  systemUptime: number;
  responseTime: number;
  errorRate: number;
}