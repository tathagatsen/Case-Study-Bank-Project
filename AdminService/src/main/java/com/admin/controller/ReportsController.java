package com.admin.controller;

import com.admin.services.ReportService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@RestController
//// MAPPING /reports
//@RequestMapping("/reports")
//public class ReportsController {
//
//    private final ReportService service;
//
//    public ReportsController(ReportService service) {
//        this.service = service;
//    }
//
//    // GET /transactions/{transactionId}
//    @GetMapping("/transactions/{transactionId}")
//    public ResponseEntity<Map<String, Object>> getTransactionById(@PathVariable Long transactionId) {
//        return ResponseEntity.ok(service.getTransactionById(transactionId));
//    }
//
//    // GET /transactions/account/{accountNumber}
//    @GetMapping("/transactions/account/{accountNumber}")
//    public ResponseEntity<List<Map<String, Object>>> getTransactionsByAccount(@PathVariable Long accountNumber) {
//        return ResponseEntity.ok(service.getTransactionsByAccount(accountNumber));
//    }
//
//    // GET /accounts
//    @GetMapping("/accounts")
//    public ResponseEntity<List<Map<String, Object>>> getAllAccounts() {
//        return ResponseEntity.ok(service.getAllAccounts());
//    }
//}
//
//
///*
// * package com.admin.controller;
// * 
// * import com.admin.services.ReportService; import
// * org.springframework.web.bind.annotation.*;
// * 
// * import java.util.Map;
// * 
// * @RestController
// * 
// * @RequestMapping("/admin/reports") public class ReportsController {
// * 
// * private final ReportService service;
// * 
// * public ReportsController(ReportService service) { this.service = service; }
// * 
// * @GetMapping("/accounts") public Map<String, Object> accountsReport() { return
// * service.getAccountsReport(); }
// * 
// * @GetMapping("/transactions") public Map<String, Object> transactionsReport()
// * { return service.getTransactionsReport(); } }
// */
@RestController
//MAPPING /reports
@RequestMapping("/reports")
public class ReportsController {

 private final ReportService service;

 public ReportsController(ReportService service) {
     this.service = service;
 }

 // GET /transactions/{transactionId}
 @GetMapping("/transactions/{transactionId}")
 public ResponseEntity<Map<String, Object>> getTransactionById(@PathVariable Long transactionId) {
     return ResponseEntity.ok(service.getTransactionById(transactionId));
 }

 // GET /transactions/account/{accountNumber}
 @GetMapping("/transactions/account/{accountNumber}")
 public ResponseEntity<List<Map<String, Object>>> getTransactionsByAccount(@PathVariable Long accountNumber) {
     return ResponseEntity.ok(service.getTransactionsByAccount(accountNumber));
 }

 // GET /accounts
 @GetMapping("/accounts")
 public ResponseEntity<List<Map<String, Object>>> getAllAccounts() {
     return ResponseEntity.ok(service.getAllAccounts());
 }
 
// @GetMapping("/login-history")
// public ResponseEntity<List<CustomerLoginHistory>> getLoginHistoryQuery
}
