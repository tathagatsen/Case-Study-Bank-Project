import React, { useState, useMemo } from 'react';
import { Search, Clock, AlertTriangle, CheckCircle, ArrowUp } from 'lucide-react';
import { useBankingAdmin } from '../context/BankingAdminContext';
import Modal from './ui/Modal';
import Button from './ui/Button';

const SupportTickets = () => {
  const { tickets, loading, updateTicket } = useBankingAdmin();

  const [searchTerm, setSearchTerm] = useState('');
  const [priorityFilter, setPriorityFilter] = useState('all');
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // Derived counts
  const counts = useMemo(() => {
    const open = tickets.filter(t => t.status === 'open').length;
    const inProgress = tickets.filter(t => t.status === 'in-progress').length;
    const resolved = tickets.filter(t => t.status === 'resolved').length;
    return { open, inProgress, resolved };
  }, [tickets]);

  const filteredTickets = tickets.filter(ticket => {
    const matchesSearch =
      (ticket.subject || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
      (ticket.description || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
      (ticket.customerId || '').toLowerCase().includes(searchTerm.toLowerCase());
    const matchesPriority = priorityFilter === 'all' || ticket.priority === priorityFilter;
    return matchesSearch && matchesPriority;
  });

  const handleViewTicket = (ticket) => {
    setSelectedTicket(ticket);
    setIsModalOpen(true);
  };

  // Rename Escalate -> In Progress
  const handleMarkInProgress = (ticketId) => {
    updateTicket(ticketId, {
      status: 'in-progress',
      // Keep a timestamp if you track it
      inProgressAt: new Date(),
      // Optional: bump priority if you want (commented out)
      // priority: 'high',
    });
  };

  const handleResolveTicket = (ticketId) => {
    updateTicket(ticketId, {
      status: 'resolved',
      resolvedAt: new Date()
    });
  };

  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'critical': return 'bg-red-100 text-red-800';
      case 'high': return 'bg-orange-100 text-orange-800';
      case 'medium': return 'bg-yellow-100 text-yellow-800';
      case 'low': return 'bg-green-100 text-green-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'open': return 'bg-blue-100 text-blue-800';
      case 'in-progress': return 'bg-yellow-100 text-yellow-800';
      case 'resolved': return 'bg-green-100 text-green-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getSLAStatus = (ticket) => {
    const createdAt = new Date(ticket.createdAt);
    const hoursSinceCreation = (Date.now() - createdAt.getTime()) / (1000 * 60 * 60);
    const slaHours = ticket.priority === 'critical' ? 1 : ticket.priority === 'high' ? 4 : 24;
    if (ticket.status === 'resolved') return null;
    return hoursSinceCreation > slaHours ? 'breached' : 'within';
  };

  if (loading) {
    return (
      <div className="p-6">
        <div className="animate-pulse space-y-4">
          <div className="h-8 bg-gray-300 rounded w-1/4"></div>
          <div className="h-96 bg-gray-300 rounded"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="p-6">
      {/* Header */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Support Tickets</h1>
        <p className="text-gray-600 mt-1">Manage customer support tickets and SLA monitoring</p>
      </div>

      {/* Overview counts: Open, In Progress, Resolved */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center">
            <Clock className="h-8 w-8 text-blue-500 mr-3" />
            <div>
              <p className="text-2xl font-bold text-gray-900">{counts.open}</p>
              <p className="text-sm text-gray-600">Open</p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center">
            <ArrowUp className="h-8 w-8 text-yellow-500 mr-3" />
            <div>
              <p className="text-2xl font-bold text-gray-900">{counts.inProgress}</p>
              <p className="text-sm text-gray-600">In Progress</p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center">
            <CheckCircle className="h-8 w-8 text-green-500 mr-3" />
            <div>
              <p className="text-2xl font-bold text-gray-900">{counts.resolved}</p>
              <p className="text-sm text-gray-600">Resolved</p>
            </div>
          </div>
        </div>
      </div>

      {/* Controls */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-6">
        <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
            <input
              type="text"
              placeholder="Search tickets..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 pr-4 py-2 w-full border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>
          <select
            value={priorityFilter}
            onChange={(e) => setPriorityFilter(e.target.value)}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="all">All Priorities</option>
            <option value="critical">Critical</option>
            <option value="high">High</option>
            <option value="medium">Medium</option>
            <option value="low">Low</option>
          </select>
        </div>
      </div>

      {/* Tickets Table */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ticket</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">User</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredTickets.map((ticket) => {
                const createdAt = new Date(ticket.createdAt);
                return (
                  <tr key={ticket.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4">
                      <div>
                        <div className="text-sm font-medium text-gray-900">{ticket.subject}</div>
                        <div className="text-sm text-gray-500 truncate max-w-xs">{ticket.description}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {ticket.customerId || ticket.id}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(ticket.status)}`}>
                        {ticket.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {createdAt.toLocaleDateString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                      <button
                        onClick={() => handleViewTicket(ticket)}
                        className="text-blue-600 hover:text-blue-900 transition-colors"
                      >
                        View
                      </button>

                      {/* Show "In Progress" only when ticket is open */}
                      {ticket.status === 'open' && (
                        <button
                          onClick={() => handleMarkInProgress(ticket.id)}
                          className="text-yellow-600 hover:text-yellow-900 transition-colors"
                        >
                          In Progress
                        </button>
                      )}

                      {/* Allow resolve when not already resolved */}
                      {ticket.status !== 'resolved' && (
                        <button
                          onClick={() => handleResolveTicket(ticket.id)}
                          className="text-green-600 hover:text-green-900 transition-colors"
                        >
                          Resolve
                        </button>
                      )}
                    </td>
                  </tr>
                );
              })}
              {filteredTickets.length === 0 && (
                <tr>
                  <td colSpan={5} className="px-6 py-6 text-center text-gray-500">
                    No tickets found.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Ticket Details Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Ticket Details"
      >
        {selectedTicket && (
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Subject</label>
              <p className="mt-1 text-sm text-gray-900">{selectedTicket.subject}</p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Description</label>
              <p className="mt-1 text-sm text-gray-900">{selectedTicket.description}</p>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Priority</label>
                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getPriorityColor(selectedTicket.priority)}`}>
                  {selectedTicket.priority}
                </span>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Status</label>
                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(selectedTicket.status)}`}>
                  {selectedTicket.status}
                </span>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Created</label>
                <p className="mt-1 text-sm text-gray-900">
                  {new Date(selectedTicket.createdAt).toLocaleDateString()} {new Date(selectedTicket.createdAt).toLocaleTimeString()}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Last Updated</label>
                <p className="mt-1 text-sm text-gray-900">
                  {new Date(selectedTicket.updatedAt).toLocaleDateString()} {new Date(selectedTicket.updatedAt).toLocaleTimeString()}
                </p>
              </div>
            </div>

            {selectedTicket.slaBreached && (
              <div className="bg-red-50 p-4 rounded-lg">
                <div className="flex items-center">
                  <AlertTriangle className="h-4 w-4 text-red-500 mr-2" />
                  <span className="text-sm font-medium text-red-800">SLA Breached</span>
                </div>
              </div>
            )}

            <div className="flex justify-end space-x-3 pt-4">
              {/* In Progress only if currently open */}
              {selectedTicket.status === 'open' && (
                <Button
                  onClick={() => {
                    handleMarkInProgress(selectedTicket.id);
                    setIsModalOpen(false);
                  }}
                  className="bg-yellow-500 hover:bg-yellow-600"
                >
                  In Progress
                </Button>
              )}
              {/* Resolve if not resolved */}
              {selectedTicket.status !== 'resolved' && (
                <Button
                  onClick={() => {
                    handleResolveTicket(selectedTicket.id);
                    setIsModalOpen(false);
                  }}
                  className="bg-green-600 hover:bg-green-700"
                >
                  Resolve
                </Button>
              )}
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default SupportTickets;