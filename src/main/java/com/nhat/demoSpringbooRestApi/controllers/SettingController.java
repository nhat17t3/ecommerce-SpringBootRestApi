package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.SettingDTO;
import com.nhat.demoSpringbooRestApi.models.Setting;
import com.nhat.demoSpringbooRestApi.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping
    public List<Setting> getAllSettings() {
        return settingService.getAllSettings();
    }

    @GetMapping("/{key}")
    public ResponseEntity<Setting> getSettingByKey(@PathVariable String key) {
        return settingService.getSettingByKey(key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Setting> createOrUpdateSetting(@RequestBody Setting setting) {
        return ResponseEntity.ok(settingService.saveSetting(setting));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Setting> updateSetting(@PathVariable Long id, @RequestBody Setting setting) {
        if (settingService.getSettingByKey(setting.getKey()).isPresent()) {
            setting.setId(id);
            return ResponseEntity.ok(settingService.saveSetting(setting));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSetting(@PathVariable int id) {
        settingService.deleteSetting(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshSettings() {
        settingService.refreshSettings();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/bulk-update")
    public ResponseEntity<?> bulkUpdateSettings(@RequestBody List<SettingDTO> settingDTOs) {
        try {
            settingService.updateMultipleSettings(settingDTOs);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            // Trả về thông báo lỗi phù hợp
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update settings.");
        }
    }
}
