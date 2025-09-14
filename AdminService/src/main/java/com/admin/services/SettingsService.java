package com.admin.services;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class SettingsService {
    private final Map<String, String> systemSettings = new HashMap<>();

    public SettingsService() {
        systemSettings.put("theme", "light");
        systemSettings.put("notifications", "enabled");
    }

    public Map<String, String> getSettings() {
        return systemSettings;
    }

    public Map<String, String> updateSettings(Map<String, String> newSettings) {
        systemSettings.putAll(newSettings);
        return systemSettings;
    }
}
