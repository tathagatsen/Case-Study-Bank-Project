package com.admin.services;

import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.admin.dto.AccountCountsDto;
import com.admin.dto.CustomerCountDto;
import com.admin.dto.DailyTransactionCountDto;
import com.admin.dto.TransactionFlagDto;
import com.admin.feign.AccountFeignClientInterface;
import com.admin.feign.CustomerFeignClient;
import com.admin.feign.TransactionFeignClientInterface;

import java.util.List;
import java.util.Map;

//@Service
//public class MonitoringService {
//
//    @Autowired
//    private TransactionFeignClientInterface transactionFeignClient;
//
//    @Autowired
//    private AccountFeignClientInterface accountFeignClient;
//
//    @Autowired
//    private CustomerFeignClient customerFeignClient;
//
//    // 1) Get flagged transactions (for monitoring)
//    public List<TransactionFlagDto> getFlaggedTransactions() {
//        try {
//            return transactionFeignClient.getFlaggedTransactions().getBody();
//        } catch (Exception ex) {
//            return List.of();
//        }
//    }
//
//    // 2) Get account counts (ACTIVE, SUSPENDED, etc.)
//    public AccountCountsDto getAccountCounts() {
//        try {
//            return accountFeignClient.getCounts();
//        } catch (Exception ex) {
//            return null;
//        }
//    }
//
//    // 3) Get daily transaction counts between dates
//    public List<DailyTransactionCountDto> getDailyTransactionCounts(String from, String to) {
//        try {
//            return transactionFeignClient.getDailyCounts(from, to).getBody();
//        } catch (Exception ex) {
//            return List.of();
//        }
//    }
//
//    // 4) Get customer counts (total, active, inactive)
//    public CustomerCountDto getCustomerCounts() {
//        try {
//            return customerFeignClient.getUserCounts();
//        } catch (Exception ex) {
//            return null;
//        }
//    }
//}
@Service
public class MonitoringService {

    @Autowired
    private TransactionFeignClientInterface transactionFeignClient;

    @Autowired
    private AccountFeignClientInterface accountFeignClient;

    @Autowired
    private CustomerFeignClient customerFeignClient;

    // 1) Get flagged transactions (for monitoring)
    public List<TransactionFlagDto> getFlaggedTransactions() {
        try {
            return transactionFeignClient.getFlaggedTransactions().getBody();
        } catch (Exception ex) {
            return List.of();
        }
    }

    // 2) Get account counts (ACTIVE, SUSPENDED, etc.)
    public AccountCountsDto getAccountCounts() {
        try {
            return accountFeignClient.getCounts();
        } catch (Exception ex) {
            return null;
        }
    }

    // 3) Get daily transaction counts between dates
    public List<DailyTransactionCountDto> getDailyTransactionCounts(String from, String to) {
        try {
            return transactionFeignClient.getDailyCounts(from, to).getBody();
        } catch (Exception ex) {
            return List.of();
        }
    }

    // 4) Get customer counts (total, active, inactive)
    public CustomerCountDto getCustomerCounts() {
        try {
            return customerFeignClient.getUserCounts();
        } catch (Exception ex) {
            return null;
        }
    }
}
