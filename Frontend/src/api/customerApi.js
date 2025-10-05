import axios from "axios";
const API = axios.create({
  baseURL: "http://localhost:8082/customer",
  // withCredentials: true, // Uncomment if using auth cookies
});

// 1. Register new customer
export const registerCustomer = (customerDto) =>
  API.post("/register", customerDto);

// 2. Customer login (using CustomerLoginDto)
export const loginCustomer = (loginDto) =>
  API.post("/login", loginDto);

// 3. Get customer details
export const getCustomerDetails = (custId) =>
  API.get(`/details/${custId}`);

// 4. Deactivate customer
export const deactivateCustomer = (customerId) =>
  API.patch(`/${customerId}/deactivate`);

// -------------------------- Support Ticket Section -------------------------

// 5. Create support ticket
export const createSupportTicket = (ticketDto) =>
  API.post("/support-ticket", ticketDto);

// 6. List all tickets for customer
export const listSupportTickets = (customerId) =>
  API.get(`/listAllTickets/${customerId}`);

// 7. Get support ticket details
export const getSupportTicketDetails = (customerId, ticketId) =>
  API.get(`/support-ticket/${customerId}/${ticketId}`);

// 8. Mark ticket as resolved
export const markTicketResolved = (customerId, ticketId) =>
  API.put("/support-ticket/resolve", null, {
    params: { customerId, ticketId }
  });

  export const getAllTickets = () =>
  API.get('/listAllTickets');
// -------------------------- KYC Section -------------------------

// 9. Upload KYC document (CustomerKycDto)
export const uploadKycDocument = (customerKycDto) =>
  API.post("/kyc/upload", customerKycDto);

export const getAllKycByCustomer = (customerId) =>
  API.get(`/kyc/getAll/${customerId}`);

// 10. Get login history for customer
export const getLoginHistory = (customerId) =>
  API.get(`/login-history/${customerId}`);

// 11. Admin: Update KYC status
export const updateKycStatusFromAdmin = (kycStatusUpdateDto) =>
  API.post("/admin/kyc/status", kycStatusUpdateDto);

// 12. Admin: Get pending KYC for admin review
export const getPendingKycForAdmin = () =>
  API.get("/admin/kycs/pending");

// -------------------------- ChatBot Section -------------------------

// 13. Ask question to chatbot
export const askChatbotQuestion = (question) =>
  API.post("/ask-chatbot", { question });

// -------------------------- Account Section -------------------------

// 14. Create account for customer
export const createAccount = (accountRequestDto) =>
  API.post("/createAccount", accountRequestDto);


export const listAllCustomers = () => API.get("/listAll");

export const getAllKycDocuments = () => API.get("/kyc/getAll");

export const updateKyc = (CustomerKycStatusUpdateDto) => API.put('/kyc/status', CustomerKycStatusUpdateDto);