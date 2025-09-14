package com.admin.controller;

import com.admin.dto.AccountStatusChangeDto;
import com.admin.dto.AdminOverviewDto;
import com.admin.dto.AdminRequestDto;
import com.admin.dto.FlagActionDto;
import com.admin.models.Admin;
import com.admin.models.AdminActivityLog;
import com.admin.services.AdminService;
import com.admin.services.AdminUserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
//
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private AdminService adminService;

    private final AdminUserServices service;

    public AdminUserController(AdminUserServices service) {
        this.service = service;
    }

    // GET all
    @GetMapping
    public List<Admin> getAllAdmins() {
    	List<Admin> admins = service.getAllUsers();
        return admins;
    }

    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        return service.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create
    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody AdminRequestDto dto) {
        return ResponseEntity.ok(service.createUser(dto));
    }

    // PUT update
    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        return ResponseEntity.ok(service.updateUser(id, admin));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/overview")
    public ResponseEntity<AdminOverviewDto> overview(@RequestParam String from, @RequestParam String to) {
        LocalDate f = LocalDate.parse(from);
        LocalDate t = LocalDate.parse(to);
        return ResponseEntity.ok(adminService.getOverview(f, t));
    }

    @PostMapping("/transactions/{txnId}/flag")
    public ResponseEntity<Void> flag(@PathVariable Long txnId, @RequestBody FlagActionDto action) {
        adminService.flagTransactionAsAdmin(txnId, action);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transactions/{txnId}/unflag")
    public ResponseEntity<Void> unflag(@PathVariable Long txnId, @RequestBody FlagActionDto action) {
        adminService.unflagTransactionAsAdmin(txnId, action);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts/{accountId}/status")
    public ResponseEntity<Void> changeAccountStatus(@PathVariable Long accountId, @RequestBody AccountStatusChangeDto dto) {
        adminService.changeAccountStatusAsAdmin(accountId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activity/recent")
    public ResponseEntity<List<AdminActivityLog>> recentActivities() {
        return ResponseEntity.ok(adminService.getRecentActivities());
    }
}


//@RestController
//// MAPPING /admin/users
//@RequestMapping("/admin/users")
//public class AdminUserController {
//	
//	@Autowired
//	private AdminService adminService;
//
//    private final AdminUserServices service;
//
//    public AdminUserController(AdminUserServices service) {
//        this.service = service;
//    }
//
//    // GET
//    @GetMapping
//    public List<Admin> getAllAdmins() {
//        return service.getAllUsers();
//    }
//
// // GET /{id}
//    @GetMapping("/{id}")
//    public Admin getAdminById(@PathVariable Long adminRequestDto) {
//        return service.getUserById(adminRequestDto).get();
//               
//    }
//    
//    // POST
//    @PostMapping
//    public ResponseEntity<AdminRequestDto> createAdmin(@RequestBody AdminRequestDto admin) {
//        return ResponseEntity.ok(service.createUser(admin));
//    }
//
//    // PUT /{id}
//    @PutMapping("/{id}")
//    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
//        return ResponseEntity.ok(service.updateUser(id, admin));
//    }
//
//    // DELETE /{id}
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
//        service.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
//    
//    @GetMapping("/overview")
//    public ResponseEntity<AdminOverviewDto> overview(@RequestParam String from, @RequestParam String to) {
//        LocalDate f = LocalDate.parse(from);
//        LocalDate t = LocalDate.parse(to);
//        return ResponseEntity.ok(adminService.getOverview(f,t));
//    }
//
//    @PostMapping("/transactions/{txnId}/flag")
//    public ResponseEntity<Void> flag(@PathVariable Long txnId, @RequestBody FlagActionDto action) {
//        adminService.flagTransactionAsAdmin(txnId, action);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/transactions/{txnId}/unflag")
//    public ResponseEntity<Void> unflag(@PathVariable Long txnId, @RequestBody FlagActionDto action) {
//        adminService.unflagTransactionAsAdmin(txnId, action);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/accounts/{accountId}/status")
//    public ResponseEntity<Void> changeAccountStatus(@PathVariable Long accountId, @RequestBody AccountStatusChangeDto dto) {
//        adminService.changeAccountStatusAsAdmin(accountId, dto);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/activity/recent")
//    public ResponseEntity<List<AdminActivityLog>> recentActivities() {
//        return ResponseEntity.ok(adminService.getRecentActivities());
//    }
//}