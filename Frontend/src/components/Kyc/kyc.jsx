import React, { useEffect, useState } from "react";
import { Plus, Upload, FileText, CheckCircle, XCircle } from "lucide-react";
// Replace with your actual KYC management context or props
// import { useKycAdmin } from '../context/KycAdminContext';

import Button from "../button";
import KycModal from "./kycModal";
import { getAllKycByCustomer } from "../../api/customerApi";
import { useSelector } from "react-redux";


const Kyc = () => {
  // Replace the following with actual data and update function
  // const { kycDocs, loading, addKycDoc } = useKycAdmin();
  const [kycDocs, setKycDocs] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedType, setSelectedType] = useState("all");
  const [selectedStatus, setSelectedStatus] = useState("all");
    const [completedKycUpload, setCompletedKycUpload] = useState(false);

  const [isModalOpen, setIsModalOpen] = useState(false);

  const user = useSelector((state) => state.user);

  // For new KYC modal
  const [newFile, setNewFile] = useState(null);
  const [newType, setNewType] = useState("Aadhar Card");

const documentTypes = ['AADHAAR', 'PAN', 'PASSPORT'];
const statusList = ['PENDING', 'APPROVED', 'REJECTED'];

const docTypeDisplayNames = {
  'AADHAAR': 'Aadhaar Card',
  'PAN': 'PAN Card',
  'PASSPORT': 'Passport'
};

  useEffect(() => {
    getAllKycByCustomer(user.userId)
      .then((response) => {
        console.log("Fetched KYC documents:", response.data);
        setKycDocs(response.data);
      })
      .catch((err) => {
        console.error("Error fetching KYC documents:", err);
      });
  }, [completedKycUpload]);

  // Filtering
  const filteredKycDocs = kycDocs.length > 0 && kycDocs?.filter((doc) => {
    const matchesSearch = doc.fileUrl
      ?.toLowerCase()
      .includes(searchTerm.toLowerCase());
    const matchesType = selectedType === "all" || doc.docType === selectedType;
    const matchesStatus =
      selectedStatus === "all" || doc.status === selectedStatus;
    return matchesSearch && matchesType && matchesStatus;
  });

  // File upload handler (simulate only)
  const handleAddKyc = (e) => {
    e.preventDefault();
    // Simulate filepath (in a real app, you'd upload file and get path/url)

    setCompletedKycUpload(prev => !prev);
    setIsModalOpen(false);
    setNewFile(null);
    setNewType("Aadhar Card");
  };

  // UI class helpers
  const getStatusColor = (status) => {
    switch (status) {
      case "APPROVED":
        return "bg-green-100 text-green-800";
      case "PENDING":
        return "bg-yellow-100 text-yellow-800";
      case "REJECTED":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  return (
    <div className="p-6">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">KYC Management</h1>
        <p className="text-gray-600 mt-1">Manage user KYC documents</p>
      </div>
      {/* Controls */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-6">
        <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between">
          <div className="flex flex-col sm:flex-row gap-4 flex-1">
            <input
              type="text"
              placeholder="Search file path..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-4 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
            <select
              value={selectedType}
              onChange={(e) => setSelectedType(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="all">All Document Types</option>
              {documentTypes.map((type) => (
                <option key={type} value={type}>
                  {type}
                </option>
              ))}
            </select>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="all">All Status</option>
              {statusList.map((status) => (
                <option key={status} value={status}>
                  {status.charAt(0).toUpperCase() + status.slice(1)}
                </option>
              ))}
            </select>
          </div>
          <Button
            onClick={() => setIsModalOpen(true)}
            className="bg-blue-600 hover:bg-blue-700"
          >
            <Plus className="h-4 w-4 mr-2" />
            Add KYC
          </Button>
        </div>
      </div>
      {/* KYC Table */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  File Path
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Document Type
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
  {filteredKycDocs.length == 0 ? (
    <tr>
      <td colSpan={3} className="px-6 py-8 text-center text-gray-500">
        No KYC documents found.
      </td>
    </tr>
  ) : (filteredKycDocs.length > 0 && 
    filteredKycDocs.map((doc) => (
      <tr key={doc.id} className="hover:bg-gray-50 transition-colors">
        <td className="px-6 py-4 whitespace-nowrap">
          <div className="flex items-center">
            <FileText className="h-4 w-4 mr-2 text-gray-400" />
            <span>{doc.fileUrl}</span>
          </div>
        </td>
        <td>{docTypeDisplayNames[doc.docType] || doc.docType}</td>
        <td className="px-6 py-4 whitespace-nowrap">
          <span
            className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(doc.status)}`}
          >
            {doc.status === "APPROVED" && <CheckCircle className="h-3 w-3 mr-1" />}
            {doc.status === "PENDING" && <Upload className="h-3 w-3 mr-1" />}
            {doc.status === "REJECTED" && <XCircle className="h-3 w-3 mr-1" />}
            {doc.status.charAt(0).toUpperCase() + doc.status.slice(1).toLowerCase()}
          </span>
        </td>
      </tr>
    ))
  )}
</tbody>

            </table>
        </div>
      </div>
      {/* KYC Add Modal */}
      <KycModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Add New KYC"
      >
        <form className="space-y-4" onSubmit={handleAddKyc}>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Document Type
            </label>
            <select
              value={newType}
              onChange={(e) => setNewType(e.target.value)}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            >
              {documentTypes.map((type) => (
                <option key={type} value={type}>
                  {type}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Upload Document
            </label>
            <input
              type="file"
              onChange={(e) => setNewFile(e.target.files[0])}
              accept=".pdf,.jpg,.jpeg,.png"
              className="mt-1 block w-full text-sm text-gray-700"
              required
            />
          </div>
          <div className="flex justify-end space-x-3 pt-4">
            <Button
              variant="secondary"
              type="button"
              onClick={() => setIsModalOpen(false)}
            >
              Cancel
            </Button>
            <Button type="submit" className="bg-blue-600 hover:bg-blue-700">
              Add KYC
            </Button>
          </div>
        </form>
      </KycModal>
    </div>
  );
};

export default Kyc;
