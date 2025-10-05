import React, { useEffect, useState, useMemo } from "react";
import { Upload, FileText, CheckCircle, XCircle } from "lucide-react";
import Button from "../../components/button";
import KycModal from "../../components/modal"; // no longer used; safe to delete import if not used elsewhere
import { getAllKycByCustomer, getAllKycDocuments, updateKyc } from "../../api/customerApi"; // ensure updateKycStatus exists
import { useSelector } from "react-redux";
import toast from "react-hot-toast";

const documentTypes = ["AADHAAR", "PAN", "PASSPORT"];
const statusList = ["PENDING", "APPROVED", "REJECTED"];
const docTypeDisplayNames = {
  AADHAAR: "Aadhaar Card",
  PAN: "PAN Card",
  PASSPORT: "Passport",
};

const KycManagement = () => {
  const [kycDocs, setKycDocs] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedType, setSelectedType] = useState("all");
  const [selectedStatus, setSelectedStatus] = useState("all");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const user = useSelector((state) => state.user);

  // Load KYC docs
  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        setError("");
        const res = await getAllKycDocuments();
        setKycDocs(Array.isArray(res?.data) ? res.data : []);
      } catch (e) {
        console.error("Error fetching KYC documents:", e);
        setError("Failed to load KYC documents.");
      } finally {
        setLoading(false);
      }
    };
    if (user?.userId) load();
  }, [user?.userId]);

  // Filtering
  const filteredKycDocs = useMemo(() => {
    const q = searchTerm.toLowerCase();
    return (kycDocs || []).filter((doc) => {
      const matchesSearch = (doc.fielPath || "").toLowerCase().includes(q);
      const matchesType = selectedType === "all" || doc.documentType === selectedType;
      const matchesStatus = selectedStatus === "all" || doc.status === selectedStatus;
      return matchesSearch && matchesType && matchesStatus;
    });
  }, [kycDocs, searchTerm, selectedType, selectedStatus]);

  // UI helpers
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

  // Actions: Approve / Reject (optimistic update)
  const setLocalStatus = (id, status) => {
    setKycDocs((prev) =>
      prev.map((d) => (String(d.id) === String(id) ? { ...d, status } : d))
    );
  };

  const handleApprove = async (doc) => {
    const prev = doc.status;
    const reviewedAt = new Date();
    const reviewedBy = 10; // or fetch from context/auth
    const remarks = "Reviewed by admin"; // optional
    try {
       updateKyc({ ...doc, status: "APPROVED", reviewedAt, reviewedBy, remarks })
       .then((res) => {
        console.log("Approve response:", res.data);
        toast.success("Document approved.");
       }
         ).catch((e) => {console.error("Approve error:", e);});

      
    } catch (e) {
      console.error("Approve failed:", e);
      setLocalStatus(doc.id, prev); // revert on failure
      setError("Failed to approve document.");
    }
  };

  const handleReject = async (doc) => {
     const prev = doc.status;
    const reviewedAt = new Date();
    const reviewedBy = 10; // or fetch from context/auth
    const remarks = "Reviewed by admin"; // optional
    try {
       updateKyc({ ...doc, status: "REJECTED", reviewedAt, reviewedBy, remarks })
       .then((res) => {
        console.log("Approve response:", res.data);
        toast.success("Document approved.");
       }
         ).catch((e) => {console.error("Approve error:", e);});

      
    } catch (e) {
      console.error("Approve failed:", e);
      setLocalStatus(doc.id, prev); // revert on failure
      setError("Failed to approve document.");
    }
  };

  return (
    <div className="p-6">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">KYC Management</h1>
        <p className="text-gray-600 mt-1">Review and act on user KYC documents</p>
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
                  {status}
                </option>
              ))}
            </select>
          </div>

          {/* Removed Add KYC button and modal per your request */}
        </div>
      </div>

      {/* Errors */}
      {error && (
        <div className="mb-4 text-sm text-red-700 bg-red-50 border border-red-200 rounded p-3">
          {error}
        </div>
      )}

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
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan={4} className="px-6 py-8 text-center text-gray-500">
                    Loading KYC documents...
                  </td>
                </tr>
              ) : filteredKycDocs.length === 0 ? (
                <tr>
                  <td colSpan={4} className="px-6 py-8 text-center text-gray-500">
                    No KYC documents found.
                  </td>
                </tr>
              ) : (
                filteredKycDocs.map((doc) => (
                  <tr key={doc.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <FileText className="h-4 w-4 mr-2 text-gray-400" />
                        <span className="truncate max-w-xs">{doc.filePath}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {docTypeDisplayNames[doc.documentType] || doc.documentType}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span
                        className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(
                          doc.status
                        )}`}
                      >
                        {doc.status === "APPROVED" && (
                          <CheckCircle className="h-3 w-3 mr-1" />
                        )}
                        {doc.status === "PENDING" && (
                          <Upload className="h-3 w-3 mr-1" />
                        )}
                        {doc.status === "REJECTED" && (
                          <XCircle className="h-3 w-3 mr-1" />
                        )}
                        {doc.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm space-x-2">
                      <Button
                        onClick={() => handleApprove(doc)}
                        disabled={doc.status === "APPROVED"}
                        className="bg-green-600 hover:bg-green-700 disabled:opacity-50"
                      >
                        Approve
                      </Button>
                      <Button
                        onClick={() => handleReject(doc)}
                        disabled={doc.status === "REJECTED"}
                        className="bg-red-600 hover:bg-red-700 disabled:opacity-50"
                      >
                        Reject
                      </Button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add KYC modal removed */}
      {/* <KycModal ...> ... </KycModal> */}
    </div>
  );
};

export default KycManagement;