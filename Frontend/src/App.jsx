import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import "./index.css";

import { BrowserRouter, Route, Routes } from "react-router";
import Login from "./Pages/Login/login";
import Register from "./Pages/Register/register";
import Dashboard from "./Pages/Dashboard/dashboard";
import Layout from "./components/layout";
import Home from "./Pages/Home/home";
import Transaction from "./Pages/Transaction/transaction";
import Account from "./Pages/Account/account";
import { Toaster } from "react-hot-toast";
import ProtectedRoute from "./components/auth/protectedRoute";
import PublicRoute from "./components/auth/publicRoute";

import KYCVerificationPage from "./Admin/pages/KYCVerficationPage";
import AdminHomePage from "./Admin/pages/HomePage";
import AdminRoutes from "./Admin/AdminRoutes";
import Support from "./Pages/Support/support";
import { SupportIcon } from "@heroicons/react/outline";
import Deposit from "./Pages/Deposit/deposit";

import Kyc from "./components/Kyc/kyc";
import About from "./components/About";
import Resources from "./components/Resources";

function App() {
  return (
    <>
      <Toaster position="bottom-right" reverseOrder={false} />
      <BrowserRouter>
        <Routes>
          {/* <Route path = "/admin/kycverification" element = {<KYCVerificationPage/>}></Route>
    <Route path = "/admin/home" element = {<AdminHomePage/>}></Route> */}
          <Route path="/admin/*" element={<AdminRoutes />} />
          <Route
            path="/"
            element={
              <PublicRoute>
                <Home />
              </PublicRoute>
            }
          ></Route>
          <Route
            path="/login"
            element={
              <PublicRoute>
                <Login />
              </PublicRoute>
            }
          ></Route>
          <Route
            path="/register"
            element={
              <PublicRoute>
                <Register />
              </PublicRoute>
            }
          ></Route>
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Layout>
                  <Dashboard />
                </Layout>
              </ProtectedRoute>
            }
          ></Route>

          <Route
            path="/account"
            element={
              <ProtectedRoute>
                <Layout>
                  <Account />
                </Layout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/transaction"
            element={
              <ProtectedRoute>
                <Layout>
                  <Transaction />
                </Layout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/bill"
            element={
              <ProtectedRoute>
                <Layout>{/* <BillPayment /> */}</Layout>{" "}
              </ProtectedRoute>
            }
          />
          <Route
            path="/deposit"
            element={
              <Layout>
                <Deposit />{" "}
              </Layout>
            }
          />
          <Route
            path="/support"
            element={
              <Layout>
                <Support />
              </Layout>
            }
          ></Route>
          <Route
            path="/kyc"
            element={
              <Layout>
                <Kyc />
              </Layout>
            }
          ></Route>

          <Route path="/about" element={<About />} />

          <Route path="/resources" element={<Resources />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
