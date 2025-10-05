import React, { useEffect, useState } from 'react';
import {
  Clock,
  TrendingUp,
  CheckCircle,
  Search,
  ChevronDown,
  Plus,
} from 'lucide-react';
import { listSupportTickets } from '../../api/customerApi';
import CreateTicketModal from './../../components/Support/CreateSupportModal';
import ViewTicketModal from '../../components/Support/ViewTicketModal';
import { useSelector } from 'react-redux';

function Support() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isViewModalOpen, setIsViewModalOpen] = useState(false);
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [priorityFilter, setPriorityFilter] = useState('all');
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const user = useSelector((state) => state.user);

  // Map API/DB row -> UI ticket shape
  const toUiTicket = (t) => ({
    id: String(t?.TICKET_ID ?? t?.ticket_id ?? t?.id ?? ''),
    subject: t?.SUBJECT ?? t?.subject ?? '',
    description: t?.DESCRIPTION ?? t?.description ?? '',
    userId: String(t?.CUSTOMER_ID ?? t?.customer_id ?? t?.EMAIL ?? t?.email ?? ''),
    priority: String(t?.PRIORITY ?? 'low').toLowerCase(),
    status: String(t?.STATUS ?? 'open').toLowerCase(),
    createdAt: t?.CREATED_AT ? new Date(t.CREATED_AT).toISOString() : new Date().toISOString(),
    updatedAt: t?.UPDATED_AT
      ? new Date(t.UPDATED_AT).toISOString()
      : (t?.CREATED_AT ? new Date(t.CREATED_AT).toISOString() : new Date().toISOString()),
  });

  // Initial load with async IIFE (no nested functions exported or used as components)
  useEffect(() => {
    let cancelled = false;

    (async () => {
      try {
        setLoading(true);
        setError('');
        const customerId = user.userId; // TODO: replace with real source (route param/context)
        const res = await listSupportTickets(customerId);
        console.log('API response for tickets:', res);
        const rows = Array.isArray(res?.data) ? res.data : (res?.data?.tickets || []);
        const mapped = rows.map(toUiTicket);
        if (!cancelled) setTickets(mapped);
      } catch (e) {
        console.error('Failed to load tickets', e);
        if (!cancelled) setError('Failed to load tickets.');
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();

    return () => {
      cancelled = true;
    };
  }, []);

  // Stats
  const stats = {
    open: tickets.filter(t => t.status === 'open').length,
    in_progress: tickets.filter(t => t.status === 'in_progress').length,
    resolved: tickets.filter(t => t.status === 'resolved').length
  };

  // Create (local-only; wire up to backend if needed)
  const handleCreateTicket = (formData) => {
    const newTicket = {
      id: (Math.random() * 10000).toFixed(0),
      subject: formData.subject || '',
      description: formData.description || '',
      userId: String(formData.customerId || ''),
      priority: 'low',
      status: 'open',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };
    setTickets(prev => [newTicket, ...prev]);
    setIsModalOpen(false);
  };

  // View
  const handleViewTicket = (ticket) => {
    setSelectedTicket(ticket);
    setIsViewModalOpen(true);
  };

  // Escalate (local-only)
  const handleEscalateTicket = (ticketId) => {
    setTickets(prev =>
      prev.map(t =>
        String(t.id) === String(ticketId)
          ? { ...t, status: 'in_progress', priority: 'critical', updatedAt: new Date().toISOString() }
          : t
      )
    );
  };

  // Resolve (local-only)
  const handleResolveTicket = (ticketId) => {
    setTickets(prev =>
      prev.map(t =>
        String(t.id) === String(ticketId)
          ? { ...t, status: 'resolved', updatedAt: new Date().toISOString() }
          : t
      )
    );
  };

  // Helpers
  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'low': return 'bg-green-100 text-green-800';
      case 'medium': return 'bg-yellow-100 text-yellow-800';
      case 'high': return 'bg-orange-100 text-orange-800';
      case 'critical': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };
  const getStatusColor = (status) => {
    switch (status) {
      case 'open': return 'bg-blue-100 text-blue-800';
      case 'in_progress': return 'bg-red-100 text-red-800';
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

  // Filters
  const filteredTickets = tickets.filter(ticket => {
    const q = searchTerm.toLowerCase();
    const matchesSearch =
      (ticket.subject || '').toLowerCase().includes(q) ||
      (ticket.description || '').toLowerCase().includes(q) ||
      (ticket.userId || '').toLowerCase().includes(q);
    const matchesPriority = priorityFilter === 'all' || ticket.priority === priorityFilter;
    return matchesSearch && matchesPriority;
  });

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Support Tickets</h1>
          <p className="text-gray-600">Manage customer support tickets and SLA monitoring</p>
        </div>

        {loading && <div className="mb-4 text-gray-600">Loading tickets…</div>}
        {error && <div className="mb-4 text-red-600">{error}</div>}

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white rounded-xl p-6 shadow-sm border">
            <div className="flex items-center">
              <Clock className="h-6 w-6 text-blue-600 mr-3" />
              <div>
                <p className="text-3xl font-bold">{stats.open}</p>
                <p className="text-sm text-gray-600">Open</p>
              </div>
            </div>
          </div>
          <div className="bg-white rounded-xl p-6 shadow-sm border">
            <div className="flex items-center">
              <TrendingUp className="h-6 w-6 text-orange-600 mr-3" />
              <div>
                <p className="text-3xl font-bold">{stats.in_progress}</p>
                <p className="text-sm text-gray-600">In progress</p>
              </div>
            </div>
          </div>
          <div className="bg-white rounded-xl p-6 shadow-sm border">
            <div className="flex items-center">
              <CheckCircle className="h-6 w-6 text-green-600 mr-3" />
              <div>
                <p className="text-3xl font-bold">{stats.resolved}</p>
                <p className="text-sm text-gray-600">Resolved</p>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border mb-8 flex flex-col sm:flex-row gap-4">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
            <input
              type="text"
              placeholder="Search tickets..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div className="flex gap-3">
            <div className="relative">
              <select
                value={priorityFilter}
                onChange={(e) => setPriorityFilter(e.target.value)}
                className="appearance-none bg-white border border-gray-300 rounded-lg px-4 py-3 pr-8 focus:ring-2 focus:ring-blue-500"
              >
                <option value="all">All Priorities</option>
                <option value="critical">Critical</option>
                <option value="high">High</option>
                <option value="medium">Medium</option>
                <option value="low">Low</option>
              </select>
              <ChevronDown className="absolute right-2 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400 pointer-events-none" />
            </div>
            <button
              onClick={() => setIsModalOpen(true)}
              className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg font-medium flex items-center gap-2"
            >
              <Plus className="h-5 w-5" />
              Create Ticket
            </button>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-sm border overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50 border-b">
                <tr>
                  <th className="py-4 px-6 text-left text-sm font-semibold text-gray-900">Ticket</th>
                  
                  <th className="py-4 px-6 text-left text-sm font-semibold text-gray-900">Status</th>
                  <th className="py-4 px-6 text-left text-sm font-semibold text-gray-900">Created</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200">
                {filteredTickets.map(ticket => {
                  const createdAt = new Date(ticket.createdAt);
                  return (
                    <tr key={ticket.id} className="hover:bg-gray-50">
                      <td className="py-4 px-6">
                        <p className="font-medium">{ticket.subject}</p>
                        <p className="text-sm text-gray-500">{ticket.description}</p>
                      </td>
                      
                      <td className="py-4 px-6">
                        <span className={`px-3 py-1 rounded-full text-sm ${getStatusColor(ticket.status)}`}>
                          {ticket.status}
                        </span>
                      </td>
                      <td className="py-4 px-6">{createdAt.toLocaleDateString()}</td>
                      </tr>
                  );
                })}
                {filteredTickets.length === 0 && !loading && (
                  <tr>
                    <td colSpan={6} className="py-6 px-6 text-center text-gray-500">
                      No tickets found.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        {isModalOpen && (
          <CreateTicketModal
            onClose={() => setIsModalOpen(false)}
            onCreate={handleCreateTicket}
          />
        )}

        <ViewTicketModal
          isOpen={isViewModalOpen}
          onClose={() => setIsViewModalOpen(false)}
          ticket={selectedTicket}
          onEscalate={handleEscalateTicket}
          onResolve={handleResolveTicket}
          getPriorityColor={getPriorityColor}
          getStatusColor={getStatusColor}
          getSLAStatus={getSLAStatus}
        />
      </div>
    </div>
  );
}

export default Support;