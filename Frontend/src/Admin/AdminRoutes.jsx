// src/routes/AdminRoutes.jsx
import React, { useState } from "react";
import { Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Dashboard from "./components/Dashboard";
import UserManagement from "./components/UserManagement";
import TransactionMonitoring from "./components/TransactionMonitoring";
import AccountManagement from "./components/AccountManagement";
import Reports from "./components/Reports";
import SystemSettings from "./components/SystemSettings";
import SupportTickets from "./components/SupportTickets";
import AdminLogs from "./components/AdminLogs";
import SystemHealth from "./components/SystemHealth";
import { BankingAdminProvider } from "./context/BankingAdminContext";
import KycManagement from "./components/KycManagement";

export default function AdminRoutes() {
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);

  return (
    <BankingAdminProvider>
      <div className="flex h-screen bg-gray-50">
        <Sidebar
          collapsed={sidebarCollapsed}
          onToggleCollapse={() => setSidebarCollapsed(!sidebarCollapsed)}
        />

        <div
          className={`flex-1 transition-all duration-300 ${
            sidebarCollapsed ? "ml-16" : "ml-64"
          }`}
        >
          <div className="h-full overflow-auto p-4">
            <Routes>
              <Route path="dashboard" element={<Dashboard />} />
              <Route path="users" element={<UserManagement />} />
               <Route path="logs" element={<AdminLogs />} />
                 <Route path="reports" element={<Reports />} />
                 <Route path="support" element={<SupportTickets />} />
                  <Route path="health" element={<SystemHealth />} />
                   <Route path="settings" element={<SystemSettings />} />
                   <Route path="transactions" element={<TransactionMonitoring />} />
                   <Route path="accounts" element={<AccountManagement />}/>
                   <Route path="kyc-management" element={<KycManagement />} />
            
            </Routes>
          </div>
        </div>
      </div>
    </BankingAdminProvider>
  );
}
