import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const navigate = useNavigate();

  const services = [
    { label: "Accounts", path: "/account" },
    // { label: "Cards", path: "/cards" },
    // { label: "Loans", path: "/loans" },
    { label: "Deposits", path: "/deposit" },
     { label: "Pay bills", path: "/bill" },
  ];

  const [searchTerm, setSearchTerm] = useState("");

  // Filter services based on search input
  const filteredServices = services.filter((item) =>
    item.label.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="font-sans bg-blue-50 min-h-screen">
      {/* Navbar */}
      <nav className="bg-blue-700 text-white px-6 py-4 flex justify-between items-center">
       <div className="flex items-center space-x-3">
    <img
      src="https://t4.ftcdn.net/jpg/05/26/84/73/360_F_526847388_TrjpwjctqjNOUmgjvlaihYAAWPEA1xMu.jpg"
      alt="PQR Bank Logo"
      className="w-10 h-10 rounded shadow"
    />
    <span className="text-xl font-bold">PQR Bank</span>
  </div>

        <div className="space-x-6 hidden md:flex">
          {/* <a href="#" className="hover:underline">Personal</a>
          <a href="#" className="hover:underline">NRI</a>
          <a href="#" className="hover:underline">Business</a>
          <a href="#" className="hover:underline">iShop</a> */}
          <a href="/resources" className="hover:underline">Resources</a>
          <button onClick={() => navigate("/about")}>About</button>
          <a href="/support" className="hover:underline">Help</a>
          <a href="/support" className="hover:underline">Complaints</a>
        </div>
        <button
          onClick={() => navigate("/login")}
          className="bg-white text-blue-700 px-4 py-1 rounded"
        >
          Login
        </button>
      </nav>

      {/* Main Section */}
      <main className="px-4 md:px-16 py-8 bg-gradient-to-r from-blue-100 to-blue-200">
        <h1 className="text-4xl font-semibold text-blue-800 mb-6">
          Truth, Trust, Transparency
        </h1>

        {/* Search */}
        <div className="flex items-center gap-2 mb-6">
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder='Search for "Accounts", "Loans"...'
            className="flex-1 p-3 rounded shadow outline-none"
          />
          <button className="bg-blue-700 text-white p-3 rounded">🔍</button>
        </div>

        {/* Services Grid */}
        <div className="grid grid-cols-2 md:grid-cols-6 gap-4 mb-8">
          {filteredServices.map(({ label, path }) => (
            <div
              key={label}
              onClick={() => navigate(path)}
              className="bg-white p-4 rounded shadow text-center hover:bg-blue-100 cursor-pointer transition"
            >
              <div className="text-2xl">🏦</div>
              <div className="mt-2 font-medium">{label}</div>
            </div>
          ))}

          {/* Support Card */}
          <div
            className="bg-blue-700 text-white p-4 rounded shadow text-center cursor-pointer hover:bg-blue-800 transition"
            onClick={() => navigate("/support")}
          >
            <div className="text-sm">Get Support</div>
            <div className="font-bold">1800 1080</div>
          </div>
        </div>

        {/* Offer Section */}
        <div className="bg-white p-4 rounded shadow mb-8">
          <h2 className="font-bold text-lg mb-2 text-blue-800">Offers for you!</h2>
          <div className="flex flex-col md:flex-row justify-between items-center gap-4">
            <div>
              <p className="text-md font-semibold">Credit Card for you!</p>
              <p className="text-sm text-gray-600">
                Enjoy discounts on BookMyShow, electronics, dining & more!
              </p>
              <div className="mt-2 space-x-4">
                <button className="text-blue-700 underline">APPLY</button>
                <button className="text-blue-700 underline">DETAILS</button>
              </div>
            </div>
            <img
              src="https://www.icicibank.com/assets/images/credit-card-img.png"
              alt="Card"
              className="w-40"
            />
          </div>
        </div>

        {/* Hero Section */}
        <div className="flex flex-col md:flex-row justify-between items-center bg-blue-600 text-white rounded-lg p-6">
          <div className="max-w-md">
            <h2 className="text-2xl font-bold mb-2">Your dream home, your pride</h2>
            <p className="mb-4">
              Turn your dream into reality with an ICICI Bank Home Loan
            </p>
            <button className="bg-white text-blue-700 px-4 py-2 rounded">
              Apply Now
            </button>
          </div>
          <img
            src="https://cdn-icons-png.flaticon.com/512/2231/2231065.png"
            alt="House"
            className="w-40 mt-4 md:mt-0"
          />
        </div>
      </main>
    </div>
  );
};

export default Home;
