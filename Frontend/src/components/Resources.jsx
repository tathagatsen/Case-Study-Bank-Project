import React from "react";

const Resources = () => {
  return (
    <div className="bg-blue-50 min-h-screen px-6 md:px-16 py-10 text-gray-800 font-sans">
      <div className="max-w-5xl mx-auto">
        <h1 className="text-4xl font-bold text-blue-700 mb-6">Resources</h1>

        {/* Banking Guides */}
        <section className="mb-10">
          <h2 className="text-2xl font-semibold text-blue-600 mb-2">📘 Banking Guides</h2>
          <ul className="list-disc list-inside space-y-2">
            <li><a href="#" className="text-blue-700 underline">How to Open a Bank Account</a></li>
            <li><a href="#" className="text-blue-700 underline">Guide to Credit Cards</a></li>
            <li><a href="#" className="text-blue-700 underline">Understanding Home Loans</a></li>
            <li><a href="#" className="text-blue-700 underline">Digital Banking Safety Tips</a></li>
          </ul>
        </section>

        {/* Tools & Calculators */}
        <section className="mb-10">
          <h2 className="text-2xl font-semibold text-blue-600 mb-2">🧮 Tools & Calculators</h2>
          <ul className="list-disc list-inside space-y-2">
            <li><a href="#" className="text-blue-700 underline">EMI Calculator</a></li>
            <li><a href="#" className="text-blue-700 underline">Home Loan Eligibility Calculator</a></li>
            <li><a href="#" className="text-blue-700 underline">Fixed Deposit Interest Calculator</a></li>
          </ul>
        </section>

        {/* Downloadable Documents */}
        <section className="mb-10">
          <h2 className="text-2xl font-semibold text-blue-600 mb-2">📄 Forms & Documents</h2>
          <ul className="list-disc list-inside space-y-2">
            <li><a href="#" className="text-blue-700 underline">Account Opening Form (PDF)</a></li>
            <li><a href="#" className="text-blue-700 underline">KYC Update Form (PDF)</a></li>
            <li><a href="#" className="text-blue-700 underline">Loan Application Form (PDF)</a></li>
          </ul>
        </section>

        {/* FAQ Section */}
        <section className="mb-10">
          <h2 className="text-2xl font-semibold text-blue-600 mb-2">❓ Frequently Asked Questions</h2>
          <ul className="list-disc list-inside space-y-2">
            <li>How can I reset my online banking password?</li>
            <li>What is the minimum balance requirement?</li>
            <li>How do I apply for a personal loan?</li>
            <li>What is the process for updating KYC?</li>
          </ul>
        </section>

        {/* Helpful Links */}
        <section>
          <h2 className="text-2xl font-semibold text-blue-600 mb-2">🔗 Useful External Links</h2>
          <ul className="list-disc list-inside space-y-2">
            <li><a href="https://www.rbi.org.in/" target="_blank" rel="noopener noreferrer" className="text-blue-700 underline">Reserve Bank of India (RBI)</a></li>
            <li><a href="https://www.incometax.gov.in/" target="_blank" rel="noopener noreferrer" className="text-blue-700 underline">Income Tax Department</a></li>
            <li><a href="https://uidai.gov.in/" target="_blank" rel="noopener noreferrer" className="text-blue-700 underline">UIDAI - Aadhaar Portal</a></li>
          </ul>
        </section>
      </div>
    </div>
  );
};

export default Resources;
