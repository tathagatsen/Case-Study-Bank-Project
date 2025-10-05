import React, { useState, useEffect } from 'react';
import { Server, Database, Wifi, AlertCircle, CheckCircle, Activity } from 'lucide-react';

const SystemHealth = () => {
  const [healthMetrics, setHealthMetrics] = useState({
    serverUptime: 99.95,
    databaseResponse: 25,
    apiResponse: 45,
    memoryUsage: 68,
    cpuUsage: 42,
    diskUsage: 35,
    activeConnections: 1247,
    errorRate: 0.02
  });

  const [serviceStatus, setServiceStatus] = useState({
    api: 'healthy',
    database: 'healthy',
    redis: 'warning',
    fileSystem: 'healthy',
    network: 'healthy',
    backup: 'error'
  });

  useEffect(() => {
    const interval = setInterval(() => {
      setHealthMetrics(prev => ({
        ...prev,
        databaseResponse: Math.max(10, Math.min(100, prev.databaseResponse + (Math.random() - 0.5) * 10)),
        apiResponse: Math.max(20, Math.min(150, prev.apiResponse + (Math.random() - 0.5) * 20)),
        memoryUsage: Math.max(30, Math.min(95, prev.memoryUsage + (Math.random() - 0.5) * 5)),
        cpuUsage: Math.max(10, Math.min(90, prev.cpuUsage + (Math.random() - 0.5) * 10)),
        activeConnections: Math.max(800, Math.min(2000, prev.activeConnections + Math.floor((Math.random() - 0.5) * 100)))
      }));
    }, 3000);

    return () => clearInterval(interval);
  }, []);

  const getServiceStatusColor = (status) => {
    switch (status) {
      case 'healthy': return 'text-green-600';
      case 'warning': return 'text-yellow-600';
      case 'error': return 'text-red-600';
      default: return 'text-gray-600';
    }
  };

  const getServiceStatusBg = (status) => {
    switch (status) {
      case 'healthy': return 'bg-green-100 text-green-800';
      case 'warning': return 'bg-yellow-100 text-yellow-800';
      case 'error': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getServiceStatusIcon = (status) => {
    switch (status) {
      case 'healthy': return CheckCircle;
      case 'warning': return AlertCircle;
      case 'error': return AlertCircle;
      default: return AlertCircle;
    }
  };

  // ✅ fixed: pass usage as parameter
  const getUsageColor = (usage) => {
    if (usage > 80) return 'bg-red-500';
    if (usage > 60) return 'bg-yellow-500';
    return 'bg-green-500';
  };

  return (
    <div className="p-6">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">System Health</h1>
        <p className="text-gray-600 mt-1">Monitor system performance and service status</p>
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center">
            <Server className="h-8 w-8 text-green-500 mr-3" />
            <div>
              <p className="text-2xl font-bold text-gray-900">{healthMetrics.serverUptime}%</p>
              <p className="text-sm text-gray-600">Uptime</p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center">
            <Database className="h-8 w-8 text-blue-500 mr-3" />
            <div>
              <p className="text-2xl font-bold text-gray-900">{Math.round(healthMetrics.databaseResponse)}ms</p>
              <p className="text-sm text-gray-600">DB Response</p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center">
            <Wifi className="h-8 w-8 text-purple-500 mr-3" />
            <div>
              <p className="text-2xl font-bold text-gray-900">{Math.round(healthMetrics.apiResponse)}ms</p>
              <p className="text-sm text-gray-600">API Response</p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center">
            <AlertCircle className="h-8 w-8 text-red-500 mr-3" />
            <div>
              <p className="text-2xl font-bold text-gray-900">{healthMetrics.errorRate}%</p>
              <p className="text-sm text-gray-600">Error Rate</p>
            </div>
          </div>
        </div>
      </div>

      {/* Resource Usage + Service Status */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        {/* Resource Usage */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Resource Usage</h2>
          <div className="space-y-4">
            {[
              { label: 'Memory Usage', value: healthMetrics.memoryUsage },
              { label: 'CPU Usage', value: healthMetrics.cpuUsage },
              { label: 'Disk Usage', value: healthMetrics.diskUsage }
            ].map(({ label, value }) => (
              <div key={label}>
                <div className="flex justify-between text-sm mb-1">
                  <span className="text-gray-700">{label}</span>
                  <span className="text-gray-900 font-medium">{Math.round(value)}%</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div 
                    className={`h-2 rounded-full ${getUsageColor(value)} transition-all duration-300`} 
                    style={{ width: `${value}%` }}
                  ></div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Service Status */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Service Status</h2>
          <div className="space-y-3">
            {Object.entries(serviceStatus).map(([service, status]) => {
              const StatusIcon = getServiceStatusIcon(status);
              return (
                <div key={service} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div className="flex items-center">
                    <StatusIcon className={`h-5 w-5 mr-3 ${getServiceStatusColor(status)}`} />
                    <span className="text-sm font-medium text-gray-900 capitalize">{service}</span>
                  </div>
                  <span className={`px-2.5 py-0.5 rounded-full text-xs font-medium ${getServiceStatusBg(status)}`}>
                    {status}
                  </span>
                </div>
              );
            })}
          </div>
        </div>
      </div>

      {/* Live Metrics */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-semibold text-gray-900">Live System Metrics</h2>
          <div className="flex items-center text-sm text-green-600">
            <Activity className="h-4 w-4 mr-1 animate-pulse" />
            Live
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="text-center">
            <div className="text-3xl font-bold text-blue-600 mb-2">
              {healthMetrics.activeConnections.toLocaleString()}
            </div>
            <div className="text-sm text-gray-600">Active Connections</div>
          </div>
          <div className="text-center">
            <div className="text-3xl font-bold text-green-600 mb-2">
              {Math.round(healthMetrics.databaseResponse)}ms
            </div>
            <div className="text-sm text-gray-600">Avg DB Response</div>
          </div>
          <div className="text-center">
            <div className="text-3xl font-bold text-purple-600 mb-2">
              {Math.round(healthMetrics.apiResponse)}ms
            </div>
            <div className="text-sm text-gray-600">Avg API Response</div>
          </div>
        </div>

        <div className="mt-6 p-4 bg-gray-50 rounded-lg">
          <h3 className="text-sm font-medium text-gray-700 mb-2">System Status</h3>
          <div className="flex items-center">
            <CheckCircle className="h-5 w-5 text-green-500 mr-2" />
            <span className="text-sm text-gray-900">
              All systems operational - Last checked: {new Date().toLocaleTimeString()}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SystemHealth;
