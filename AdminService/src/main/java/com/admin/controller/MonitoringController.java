package com.admin.controller;

import com.admin.dto.AccountCountsDto;
import com.admin.dto.CustomerCountDto;
import com.admin.dto.DailyTransactionCountDto;
import com.admin.dto.TransactionFlagDto;
import com.admin.services.MonitoringService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@RestController
//// MAPPING /monitoring
//@RequestMapping("/monitoring")
//public class MonitoringController {
//
//    private final MonitoringService service;
//
//    public MonitoringController(MonitoringService service) {
//        this.service = service;
//    }
//
//    // GET /transactions
//    @GetMapping("/transactions")
//    public List<Map<String, Object>> monitorTransactions() {
//        return service.getDummyTransactions();
//    }
//
//    // GET /accounts
//    @GetMapping("/accounts")
//    public List<Map<String, Object>> monitorAccounts() {
//        return service.getDummyAccounts();
//    }
//}
//
//
//
///*
// * package com.admin.controller;
// * 
// * import com.admin.services.MonitoringService; import
// * org.springframework.web.bind.annotation.*;
// * 
// * import java.util.List; import java.util.Map;
// * 
// * @RestController
// * 
// * @RequestMapping("/admin/monitor") public class MonitoringController {
// * 
// * private final MonitoringService service;
// * 
// * public MonitoringController(MonitoringService service) { this.service =
// * service; }
// * 
// * @GetMapping("/transactions") public List<Map<String, Object>>
// * monitorTransactions() { return service.getDummyTransactions(); }
// * 
// * @GetMapping("/accounts") public List<Map<String, Object>> monitorAccounts() {
// * return service.getDummyAccounts(); } }
// */
@RestController
@RequestMapping("/monitoring")
public class MonitoringController {
    private final MonitoringService service;
    public MonitoringController(MonitoringService service) {
        this.service = service;
    }
    // 1) Get flagged transactions
    @GetMapping("/transactions/flagged")
    public List<TransactionFlagDto> getFlaggedTransactions() {
        return service.getFlaggedTransactions();
    }
    // 2) Get account counts
    @GetMapping("/accounts/counts")
    public AccountCountsDto getAccountCounts() {
        return service.getAccountCounts();
    }
    // 3) Get daily transaction counts between dates
    @GetMapping("/transactions/daily")
    public List<DailyTransactionCountDto> getDailyTransactionCounts(
            @RequestParam String from,
            @RequestParam String to) {
        return service.getDailyTransactionCounts(from, to);
    }
    // 4) Get customer counts
    @GetMapping("/customers/counts")
    public CustomerCountDto getCustomerCounts() {
        return service.getCustomerCounts();
    }
}