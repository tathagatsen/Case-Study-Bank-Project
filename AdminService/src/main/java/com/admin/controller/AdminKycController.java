package com.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.admin.dto.*;
import com.admin.models.AdminKyc;
import com.admin.services.AdminKycService;

//@RestController
//@RequestMapping("/admin")
//public class AdminKycController {
//	@Autowired
//    private  AdminKycService adminKycService;
//
//    // Called by Customer service (push)
//    @PostMapping("/send-kyc-details")
//    public ResponseEntity<Void> receiveKyc(@RequestBody CustomerAdminKycDto dto) {
//        adminKycService.receiveKycFromCustomer(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
//    // Admin UI actions
//    @PostMapping("/kyc/{id}/approve")
//    public ResponseEntity<Void> approve(@PathVariable Long id, @RequestBody AdminActionDto action) {
//        adminKycService.approveKyc(id, action);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/kyc/{id}/reject")
//    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestBody AdminActionDto action) {
//        adminKycService.rejectKyc(id, action);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/kyc/pending")
//    public ResponseEntity<List<AdminKyc>> pending() {
//        return ResponseEntity.ok(adminKycService.getPending());
//    }
//}
@RestController
@RequestMapping("/admin")
public class AdminKycController {
	@Autowired
    private  AdminKycService adminKycService;

    // Called by Customer service (push)
    @PostMapping("/send-kyc-details")
    public ResponseEntity<Void> receiveKyc(@RequestBody CustomerAdminKycDto dto) {
        adminKycService.receiveKycFromCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Admin UI actions
    @PostMapping("/kyc/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id, @RequestBody AdminActionDto action) {
        adminKycService.approveKyc(id, action);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/kyc/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestBody AdminActionDto action) {
        adminKycService.rejectKyc(id, action);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/kyc/pending")
    public ResponseEntity<List<AdminKyc>> pending() {
        return ResponseEntity.ok(adminKycService.getPending());
    }
    
    @Autowired
    private AdminKycService service;

    @PostMapping("/kyc/review")
    public ResponseEntity<String> reviewKyc(@RequestBody CustomerAdminKycDto dto) {
        service.reviewKyc(dto);
        return ResponseEntity.ok("KYC reviewed successfully");
    }
}
