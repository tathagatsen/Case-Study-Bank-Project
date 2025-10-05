import { useState } from "react";

export default function KYCVerificationPage() {
  const [status, setStatus] = useState("Pending");

  const customer = {
    name: "John Doe",
    customerId: "CUST12345",
    dob: "01 Jan 1990",
    gender: "Male",
    email: "john.doe@email.com",
    phone: "+91 98765 43210",
    address: "Mumbai, India",
    nationality: "Indian",
    occupation: "Software Engineer",
   documents: {
  identity: { type: "PAN Card", link: "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf" },
  address: { type: "Utility Bill", link: "https://via.placeholder.com/400x250.png?text=Utility+Bill" },
  photo: { type: "Passport Size Photo", link: "https://randomuser.me/api/portraits/men/32.jpg" },
  signature: { type: "Digital Signature", link: "https://via.placeholder.com/200x80.png?text=Signature" },
  income: { type: "Salary Slip", link: "https://www.orimi.com/pdf-test.pdf" },
},

  };

  return (
    <div className="p-6 grid grid-cols-1 lg:grid-cols-2 gap-6 bg-gray-50 min-h-screen">
      {/* Customer Info */}
      <div className="bg-white shadow-xl rounded-2xl p-6 space-y-3">
        <h2 className="text-xl font-semibold border-b pb-2">Customer Information</h2>
        <p><b>Name:</b> {customer.name}</p>
        <p><b>Customer ID:</b> {customer.customerId}</p>
        <p><b>Date of Birth:</b> {customer.dob}</p>
        <p><b>Gender:</b> {customer.gender}</p>
        <p><b>Email:</b> {customer.email}</p>
        <p><b>Phone:</b> {customer.phone}</p>
        <p><b>Address:</b> {customer.address}</p>
        <p><b>Nationality:</b> {customer.nationality}</p>
        <p><b>Occupation:</b> {customer.occupation}</p>
      </div>

      {/* Documents */}
      <div className="bg-white shadow-xl rounded-2xl p-6 space-y-3">
        <h2 className="text-xl font-semibold border-b pb-2">Uploaded Documents</h2>
        <div className="space-y-2">
          <p><b>Identity Proof:</b> {customer.documents.identity.type} – <a href={customer.documents.identity.link} className="text-blue-600 hover:underline">View</a></p>
          <p><b>Address Proof:</b> {customer.documents.address.type} – <a href={customer.documents.address.link} className="text-blue-600 hover:underline">View</a></p>
          <p><b>Photo:</b> {customer.documents.photo.type} – <a href={customer.documents.photo.link} className="text-blue-600 hover:underline">View</a></p>
          <p><b>Signature:</b> {customer.documents.signature.type} – <a href={customer.documents.signature.link} className="text-blue-600 hover:underline">View</a></p>
          <p><b>Income Proof:</b> {customer.documents.income.type} – <a href={customer.documents.income.link} className="text-blue-600 hover:underline">View</a></p>
        </div>
      </div>

      {/* Approval Section */}
      <div className="lg:col-span-2 bg-white shadow-xl rounded-2xl p-6 space-y-4">
        <h2 className="text-xl font-semibold border-b pb-2">KYC Verification</h2>

        {/* Current Status */}
        <p className="text-lg">
          Current Status:{" "}
          <span
            className={`font-bold ${
              status === "Approved"
                ? "text-green-600"
                : status === "Rejected"
                ? "text-red-600"
                : "text-yellow-600"
            }`}
          >
            {status}
          </span>
        </p>

        {/* Action Buttons */}
        <div className="flex gap-3">
          <button
            onClick={() => setStatus("Approved")}
            className="px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg shadow-md"
          >
            ✅ Approve
          </button>
          <button
            onClick={() => setStatus("Rejected")}
            className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-lg shadow-md"
          >
            ❌ Reject
          </button>
        </div>
      </div>
    </div>
  );
}