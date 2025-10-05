// src/Admin/components/layout/Sidebar.jsx
import React from "react";
import {
  LayoutDashboard,
  Users,
  CreditCard,
  Activity,
  FileText,
  Settings,
  Headphones,
  Shield,
  HeartPulse,
  ChevronLeft,
  ChevronRight,
  Building2,
} from "lucide-react";
import { NavLink } from "react-router-dom";

const Sidebar = ({ collapsed, onToggleCollapse }) => {
  const menuItems = [
    { id: "dashboard", label: "Dashboard", icon: LayoutDashboard },
    { id: "users", label: "User Management", icon: Users },
    { id: "accounts", label: "Account Management", icon: CreditCard },
    { id: "transactions", label: "Transaction Monitor", icon: Activity },
    { id: "reports", label: "Reports", icon: FileText },
    { id: "support", label: "Support Tickets", icon: Headphones },
    { id: "kyc-management", label: "Kyc Management", icon: Settings },
    // { id: "logs", label: "Admin Logs", icon: Shield },
    // { id: "health", label: "System Health", icon: HeartPulse },
  ];

  return (
    <aside
      className={`bg-slate-900 text-white transition-all duration-300 fixed left-0 top-0 h-full z-10 
      ${collapsed ? "w-16" : "w-64"}`}
    >
      {/* Header */}
      <div className="flex items-center justify-between p-4 border-b border-slate-700">
        {!collapsed && (
          <div className="flex items-center space-x-2">
            <Building2 className="h-8 w-8 text-blue-400" />
            <span className="text-xl font-bold">BankAdmin</span>
          </div>
        )}
        <button
          onClick={onToggleCollapse}
          className="p-1 rounded-lg hover:bg-slate-700 transition-colors"
        >
          {collapsed ? <ChevronRight size={20} /> : <ChevronLeft size={20} />}
        </button>
      </div>

      {/* Menu */}
      <nav className="mt-6">
        {menuItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.id}
              to={`/admin/${item.id}`}
              className={({ isActive }) =>
                `w-full flex items-center px-4 py-3 text-left hover:bg-slate-700 transition-colors ${
                  isActive ? "bg-slate-700 border-r-2 border-blue-400" : ""
                }`
              }
            >
              {({ isActive }) => (
                <>
                  <Icon
                    size={20}
                    className={`${
                      isActive ? "text-blue-400" : "text-slate-300"
                    } flex-shrink-0`}
                  />
                  {!collapsed && (
                    <span
                      className={`ml-3 ${
                        isActive ? "text-white font-medium" : "text-slate-300"
                      }`}
                    >
                      {item.label}
                    </span>
                  )}
                </>
              )}
            </NavLink>
          );
        })}
      </nav>
    </aside>
  );
};

export default Sidebar;
