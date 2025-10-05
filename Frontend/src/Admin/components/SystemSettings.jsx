// src/Admin/components/SystemSettings.jsx
import React, { useState } from 'react';
import { Save, RefreshCw, Shield, Bell, Database, Globe } from 'lucide-react';
import Button from './ui/Button';

const SystemSettings = () => {
  const [settings, setSettings] = useState({
    security: {
      sessionTimeout: '30',
      maxLoginAttempts: '5',
      passwordMinLength: '8',
      twoFactorRequired: true,
      ipWhitelistEnabled: false
    },
    notifications: {
      emailAlerts: true,
      smsAlerts: false,
      fraudAlerts: true,
      systemAlerts: true,
      slaBreachAlerts: true
    },
    system: {
      maintenanceMode: false,
      debugLogging: false,
      auditLogging: true,
      apiRateLimit: '1000',
      backupFrequency: 'daily'
    },
    business: {
      dailyTransactionLimit: '100000',
      fraudThreshold: '10000',
      currencyCode: 'INR',
      businessHours: '09:00-17:00',
      timezone: 'UTC'
    }
  });

  const handleSave = () => {
    console.log('Saving settings:', settings);
    alert('Settings saved successfully!');
  };

  const handleReset = () => {
    setSettings({
      security: {
        sessionTimeout: '30',
        maxLoginAttempts: '5',
        passwordMinLength: '8',
        twoFactorRequired: true,
        ipWhitelistEnabled: false
      },
      notifications: {
        emailAlerts: true,
        smsAlerts: false,
        fraudAlerts: true,
        systemAlerts: true,
        slaBreachAlerts: true
      },
      system: {
        maintenanceMode: false,
        debugLogging: false,
        auditLogging: true,
        apiRateLimit: '1000',
        backupFrequency: 'daily'
      },
      business: {
        dailyTransactionLimit: '100000',
        fraudThreshold: '10000',
        currencyCode: 'INR',
        businessHours: '09:00-17:00',
        timezone: 'UTC'
      }
    });
  };

  const updateSetting = (category, key, value) => {
    setSettings(prev => ({
      ...prev,
      [category]: {
        ...prev[category],
        [key]: value
      }
    }));
  };

  return (
    <div className="p-6">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">System Settings</h1>
        <p className="text-gray-600 mt-1">Configure system parameters and business rules</p>
      </div>

      <div className="space-y-6">
        {/* Security Settings */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center mb-4">
            <Shield className="h-5 w-5 text-blue-600 mr-2" />
            <h2 className="text-xl font-semibold text-gray-900">Security Settings</h2>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Session Timeout (minutes)</label>
              <input
                type="number"
                value={settings.security.sessionTimeout}
                onChange={(e) => updateSetting('security', 'sessionTimeout', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Max Login Attempts</label>
              <input
                type="number"
                value={settings.security.maxLoginAttempts}
                onChange={(e) => updateSetting('security', 'maxLoginAttempts', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Password Minimum Length</label>
              <input
                type="number"
                value={settings.security.passwordMinLength}
                onChange={(e) => updateSetting('security', 'passwordMinLength', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <div className="space-y-3">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={settings.security.twoFactorRequired}
                  onChange={(e) => updateSetting('security', 'twoFactorRequired', e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm text-gray-700">Require Two-Factor Authentication</span>
              </label>
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={settings.security.ipWhitelistEnabled}
                  onChange={(e) => updateSetting('security', 'ipWhitelistEnabled', e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm text-gray-700">Enable IP Whitelist</span>
              </label>
            </div>
          </div>
        </div>

        {/* Notification Settings */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center mb-4">
            <Bell className="h-5 w-5 text-green-600 mr-2" />
            <h2 className="text-xl font-semibold text-gray-900">Notification Settings</h2>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="space-y-3">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={settings.notifications.emailAlerts}
                  onChange={(e) => updateSetting('notifications', 'emailAlerts', e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm text-gray-700">Email Alerts</span>
              </label>
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={settings.notifications.smsAlerts}
                  onChange={(e) => updateSetting('notifications', 'smsAlerts', e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm text-gray-700">SMS Alerts</span>
              </label>
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={settings.notifications.fraudAlerts}
                  onChange={(e) => updateSetting('notifications', 'fraudAlerts', e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm text-gray-700">Fraud Detection Alerts</span>
              </label>
            </div>
            <div className="space-y-3">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={settings.notifications.systemAlerts}
                  onChange={(e) => updateSetting('notifications', 'systemAlerts', e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm text-gray-700">System Alerts</span>
              </label>
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={settings.notifications.slaBreachAlerts}
                  onChange={(e) => updateSetting('notifications', 'slaBreachAlerts', e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm text-gray-700">SLA Breach Alerts</span>
              </label>
            </div>
          </div>
        </div>

        {/* System Configuration */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center mb-4">
            <Database className="h-5 w-5 text-purple-600 mr-2" />
            <h2 className="text-xl font-semibold text-gray-900">System Configuration</h2>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">API Rate Limit (per hour)</label>
              <input
                type="number"
                value={settings.system.apiRateLimit}
                onChange={(e) => updateSetting('system', 'apiRateLimit', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Backup Frequency</label>
              <select
                value={settings.system.backupFrequency}
                onChange={(e) => updateSetting('system', 'backupFrequency', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="hourly">Hourly</option>
                <option value="daily">Daily</option>
                <option value="weekly">Weekly</option>
              </select>
            </div>
            <div className="space-y-3">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={settings.system.maintenanceMode}
                  onChange={(e) => updateSetting('system', 'maintenanceMode', e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm text-gray-700">Maintenance Mode</span>
              </label>
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={settings.system.debugLogging}
                  onChange={(e) => updateSetting('system', 'debugLogging', e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm text-gray-700">Debug Logging</span>
              </label>
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={settings.system.auditLogging}
                  onChange={(e) => updateSetting('system', 'auditLogging', e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm text-gray-700">Audit Logging</span>
              </label>
            </div>
          </div>
        </div>

        {/* Business Rules */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div className="flex items-center mb-4">
            <Globe className="h-5 w-5 text-orange-600 mr-2" />
            <h2 className="text-xl font-semibold text-gray-900">Business Rules</h2>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Daily Transaction Limit ($)</label>
              <input
                type="number"
                value={settings.business.dailyTransactionLimit}
                onChange={(e) => updateSetting('business', 'dailyTransactionLimit', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Fraud Detection Threshold ($)</label>
              <input
                type="number"
                value={settings.business.fraudThreshold}
                onChange={(e) => updateSetting('business', 'fraudThreshold', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Business Hours</label>
              <input
                type="text"
                value={settings.business.businessHours}
                onChange={(e) => updateSetting('business', 'businessHours', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Timezone</label>
              <select
                value={settings.business.timezone}
                onChange={(e) => updateSetting('business', 'timezone', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="UTC">UTC</option>
                <option value="EST">EST</option>
                <option value="PST">PST</option>
                <option value="GMT">GMT</option>
              </select>
            </div>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex justify-end space-x-4">
          <Button
            variant="secondary"
            onClick={handleReset}
          >
            <RefreshCw className="h-4 w-4 mr-2" />
            Reset to Defaults
          </Button>
          <Button
            onClick={handleSave}
            className="bg-green-600 hover:bg-green-700"
          >
            <Save className="h-4 w-4 mr-2" />
            Save Changes
          </Button>
        </div>
      </div>
    </div>
  );
};

export default SystemSettings;
