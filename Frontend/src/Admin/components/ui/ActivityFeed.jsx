import React from 'react';
import { Clock } from 'lucide-react';

const ActivityFeed = ({ activities }) => {
  const getSeverityColor = (severity) => {
    switch (severity) {
      case 'low': return 'bg-blue-100 text-blue-800';
      case 'medium': return 'bg-yellow-100 text-yellow-800';
      case 'high': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
      <div className="flex items-center mb-4">
        <Clock className="h-5 w-5 text-gray-500 mr-2" />
        <h3 className="text-lg font-semibold text-gray-900">Recent Activity</h3>
      </div>
      <div className="space-y-3">
        {activities.map((activity) => (
          <div
            key={activity.id}
            className="flex items-start space-x-3 p-3 rounded-lg hover:bg-gray-50 transition-colors"
          >
            <div
              className={`w-2 h-2 rounded-full mt-2 ${
                activity.severity === 'high'
                  ? 'bg-red-500'
                  : activity.severity === 'medium'
                  ? 'bg-yellow-500'
                  : 'bg-blue-500'
              }`}
            ></div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900">{activity.action}</p>
              <p className="text-sm text-gray-600">by {activity.user}</p>
              <span
                className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium mt-1 ${getSeverityColor(
                  activity.severity
                )}`}
              >
                {activity.severity}
              </span>
            </div>
            <div className="text-xs text-gray-500">{activity.time}</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ActivityFeed;
