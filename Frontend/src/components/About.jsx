import React from "react";

const About = () => {
  return (
    <div className="bg-blue-50 min-h-screen px-6 md:px-16 py-10 text-gray-800 font-sans">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-4xl font-bold text-blue-700 mb-6">About PQR Bank</h1>

        {/* Bank Introduction */}
        <section className="mb-8">
          <p className="text-lg leading-relaxed">
            <strong>PQR Bank</strong> is a next-generation digital banking platform committed to delivering 
            fast, secure, and customer-centric financial solutions. Whether you're looking to manage your 
            savings, apply for a loan, or invest in your future, PQR Bank is your reliable partner in financial 
            growth and stability.
          </p>
        </section>

        {/* Mission & Vision */}
        <section className="mb-8">
          <h2 className="text-2xl font-semibold text-blue-600 mb-2">Our Mission</h2>
          <p className="leading-relaxed">
            To empower individuals and businesses with cutting-edge banking solutions that are simple, transparent, and trustworthy.
          </p>

          <h2 className="text-2xl font-semibold text-blue-600 mt-6 mb-2">Our Core Values</h2>
          <ul className="list-disc list-inside space-y-1">
            <li>Integrity & Transparency</li>
            <li>Customer First Approach</li>
            <li>Innovation Driven</li>
            <li>Security & Privacy Focused</li>
            <li>Inclusive Financial Growth</li>
          </ul>
        </section>

        {/* Services */}
        <section className="mb-8">
          <h2 className="text-2xl font-semibold text-blue-600 mb-2">What We Offer</h2>
          <ul className="list-disc list-inside space-y-1">
            <li>Savings & Current Accounts</li>
            <li>Credit & Debit Cards</li>
            <li>Personal, Home & Business Loans</li>
            <li>Fixed & Recurring Deposits</li>
            <li>Online Investment & Mutual Funds</li>
            <li>24/7 Customer Support</li>
            <li>Secure Mobile & Internet Banking</li>
          </ul>
        </section>

        {/* Security */}
        <section className="mb-8">
          <h2 className="text-2xl font-semibold text-blue-600 mb-2">Security You Can Trust</h2>
          <p className="leading-relaxed">
            Your data and finances are protected with bank-grade security infrastructure. We use advanced 
            encryption, 2-factor authentication, fraud monitoring, and more to keep your transactions safe.
          </p>
        </section>

        {/* Contact Info */}
        <section>
          <h2 className="text-2xl font-semibold text-blue-600 mb-2">Contact Us</h2>
          <p>
            📞 <strong>Customer Support:</strong> 1800 1080
          </p>
          <p>
            📧 <strong>Email:</strong> support@pqrbank.com
          </p>
          <p>
            🌐 <strong>Website:</strong> www.pqrbank.com
          </p>
        </section>
      </div>
    </div>
  );
};

export default About;
