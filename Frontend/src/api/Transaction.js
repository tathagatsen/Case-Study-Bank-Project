import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8084/transactions",
  // withCredentials: true, // if needed for auth/cookies
});

// 1. Create a transaction
export const createTransaction = (transactionRequestDto) =>
  API.post("/create", transactionRequestDto);

// 2. Get transaction by ID
export const getTransactionById = (id) =>
  API.get(`/get/${id}`);



// 3. Get all transactions for an account
export const getTransactionsForAccount = (accountId) =>
  API.get(`/account/${accountId}`);


// 4. Get mini statement for account (default 5, or pass limit)
export const getMiniStatement = (accountId, limit = 5) =>
  API.get(`/account/${accountId}/mini-statement`, { params: { limit } });

// 5. Flag a transaction
export const flagTransaction = (id, flagActionDto) =>
  API.post(`/${id}/flag`, flagActionDto);

// 6. Unflag a transaction
export const unflagTransaction = (id, flagActionDto) =>
  API.post(`/${id}/unflag`, flagActionDto);

// 7. Get all flagged transactions (Admin)
export const getFlaggedTransactions = () =>
  API.get("/admin/flagged");

// 8. Get daily counts for admin (pass from/to as YYYY-MM-DD)
export const getDailyCounts = (from, to) =>
  API.get("/admin/daily-counts", { params: { from, to } });

export const getAllTransaction = () =>
  API.get("/all");