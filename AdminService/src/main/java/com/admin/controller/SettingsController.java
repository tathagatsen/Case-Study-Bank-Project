package com.admin.controller;

import com.admin.services.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
// MAPPING /settings
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService service;

    public SettingsController(SettingsService service) {
        this.service = service;
    }

    // GET /settings
    @GetMapping
    public ResponseEntity<Map<String, String>> getSettings() {
        return ResponseEntity.ok(service.getSettings());
    }

    // PUT /settings
    @PutMapping
    public ResponseEntity<Map<String, String>> updateSettings(@RequestBody Map<String, String> settings) {
        return ResponseEntity.ok(service.updateSettings(settings));
    }
}



/*
 * package com.admin.controller;
 * 
 * import com.admin.services.SettingsService; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import java.util.Map;
 * 
 * @RestController
 * 
 * @RequestMapping("/admin/settings") public class SettingsController {
 * 
 * private final SettingsService service;
 * 
 * public SettingsController(SettingsService service) { this.service = service;
 * }
 * 
 * @PutMapping public Map<String, String> updateSettings(@RequestBody
 * Map<String, String> settings) { return service.updateSettings(settings); } }
 */