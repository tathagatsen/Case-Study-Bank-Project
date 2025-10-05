import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8083/accounts", // base path for AccountController
});

// 1. Create Account
export const createAccount = (accountData) =>
  API.post("/createAccount", accountData);

// 2. Get Account by ID
export const getAccountById = (customerId) =>
  API.get(`/customers/${customerId}/accounts`);

// 3. Get Account by Account Number
export const getAccountByNumber = (accountNumber) =>
  API.get(`/number/${accountNumber}`);

// 4. Get Accounts by Customer ID
export const getAccountsByCustomer = (customerId) =>
  API.get(`/customers/${customerId}/accounts`);


export const getTransactionsForCustomer = (customerId) =>
  API.get(`customers/${customerId}/transactions`);

// 5. Get Balance info
export const getAccountBalance = (accountId) =>
  API.get(`/${accountId}/balance`);

// 6. Update Balance
export const updateBalance = (accountId, updateData) =>
  API.patch(`/${accountId}/update-balance`, updateData);

// 7. Create Transaction
export const createTransaction = (transactionData) =>
  API.post("/createTransaction", transactionData);

// 8. Get All Accounts
export const getAllAccounts = () =>
  API.get("/all");

export const getAllBranches = () => 
  API.get("/getAllBranches");
