import React from "react";
import { X, AlertTriangle } from "lucide-react";

const ViewTicketModal = ({
  onClose,
  ticket,
  onEscalate,
  onResolve,
  getPriorityColor,
  getStatusColor,
  getSLAStatus,
}) => {
  if (!ticket) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="p-6 border-b border-gray-200 flex items-center justify-between">
          <h2 className="text-xl font-semibold text-gray-900">
            Ticket Details
          </h2>
          <button
            onClick={onClose}
            className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <X className="h-5 w-5 text-gray-500" />
          </button>
        </div>

        {/* Body */}
        <div className="p-6 space-y-6">
          <div>
            <p className="font-medium text-gray-700">Title</p>
            <p className="text-gray-900">{ticket.title}</p>
          </div>

          <div>
            <p className="font-medium text-gray-700">Description</p>
            <p className="text-gray-900">{ticket.description}</p>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <p className="font-medium text-gray-700">Priority</p>
              <span
                className={`px-3 py-1 rounded-full text-sm font-medium ${getPriorityColor(
                  ticket.priority
                )}`}
              >
                {ticket.priority}
              </span>
            </div>
            <div>
              <p className="font-medium text-gray-700">Status</p>
              <span
                className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(
                  ticket.status
                )}`}
              >
                {ticket.status}
              </span>
            </div>
          </div>

          <div>
            <p className="font-medium text-gray-700">Created</p>
            <p className="text-gray-900">{ticket.created}</p>
          </div>

          {getSLAStatus && getSLAStatus(ticket) === "breached" && (
            <div className="bg-red-50 border border-red-200 text-red-700 p-4 rounded-lg flex items-center gap-2 text-sm">
              <AlertTriangle className="h-5 w-5" />
              SLA Breached
            </div>
          )}

          {/* Actions */}
          <div className="flex justify-end gap-3 pt-4">
            {ticket.status !== "escalated" && ticket.status !== "resolved" && (
              <button
                onClick={() => {
                  onEscalate(ticket.id);
                  onClose();
                }}
                className="px-6 py-3 bg-orange-600 text-white rounded-lg font-medium hover:bg-orange-700 transition-colors"
              >
                Escalate
              </button>
            )}
            {ticket.status !== "resolved" && (
              <button
                onClick={() => {
                  onResolve(ticket.id);
                  onClose();
                }}
                className="px-6 py-3 bg-green-600 text-white rounded-lg font-medium hover:bg-green-700 transition-colors"
              >
                Resolve
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewTicketModal;
