import React, { useState, useEffect } from "react";
// if you face import errors, replace with: import TrendingUp from "lucide-react/dist/esm/icons/trending-up";
import { TrendingUp } from "lucide-react";

const LiveMetricsCard = ({ title, value, subtitle }) => {
  const [isUpdating, setIsUpdating] = useState(false);

  useEffect(() => {
    const interval = setInterval(() => {
      setIsUpdating(true);
      setTimeout(() => setIsUpdating(false), 200);
    }, 5000);

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-lg font-semibold text-gray-900">{title}</h3>
        <TrendingUp className="h-5 w-5 text-green-500" />
      </div>
      <div
        className={`transition-all duration-200 ${
          isUpdating ? "scale-105" : "scale-100"
        }`}
      >
        <p className="text-3xl font-bold text-gray-900 mb-1">{value}</p>
        <p className="text-sm text-gray-600">{subtitle}</p>
      </div>
      <div className="mt-4 h-2 bg-gray-200 rounded-full overflow-hidden">
        <div
          className="h-full bg-blue-500 rounded-full animate-pulse"
          style={{ width: "75%" }}
        ></div>
      </div>
    </div>
  );
};

export default LiveMetricsCard;
