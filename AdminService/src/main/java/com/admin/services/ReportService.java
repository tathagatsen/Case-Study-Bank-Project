package com.admin.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.admin.feign.AccountFeignClientInterface;
import com.admin.feign.CustomerFeignClient;
import com.admin.feign.TransactionFeignClientInterface;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//@Service
//public class ReportService {
//
//    private final RestTemplate restTemplate;
//
//    // Replace with service discovery names (Eureka/Consul) or actual service URLs
//    private static final String TRANSACTIONS_SERVICE_URL = "http://localhost:8084/transactions";
//    private static final String ACCOUNTS_SERVICE_URL = "http://localhost:8083/accounts";
//
//    public ReportService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    /**
//     * Fetch a transaction by its ID from the Transactions microservice.
//     */
//    public Map<String, Object> getTransactionById(Long transactionId) {
//        return restTemplate.getForObject(
//                TRANSACTIONS_SERVICE_URL + "/" + transactionId,
//                Map.class
//        );
//    }
//
//    /**
//     * Fetch all transactions for a given account number from the Transactions microservice.
//     */
//    public List<Map<String, Object>> getTransactionsByAccount(Long accountNumber) {
//        Map<String, Object>[] response = restTemplate.getForObject(
//                TRANSACTIONS_SERVICE_URL + "/account/" + accountNumber,
//                Map[].class
//        );
//        return Arrays.asList(response != null ? response : new Map[0]);
//    }
//
//    /**
//     * Fetch all accounts from the Accounts microservice.
//     */
//    public List<Map<String, Object>> getAllAccounts() {
//        Map<String, Object>[] response = restTemplate.getForObject(
//                ACCOUNTS_SERVICE_URL,
//                Map[].class
//        );
//        return Arrays.asList(response != null ? response : new Map[0]);
//    }
//}
@Service
public class ReportService {

    @Autowired
    private TransactionFeignClientInterface transactionsFeignClient;

    @Autowired
    private AccountFeignClientInterface accountsFeignClient;

    @Autowired
    private CustomerFeignClient customerFeignClient;

    /**
     * Fetch a transaction by its ID from the Transactions microservice.
     */
    public Map<String, Object> getTransactionById(Long transactionId) {
        return transactionsFeignClient.getTransactionById(transactionId);
    }

    /**
     * Fetch all transactions for a given account number from the Transactions microservice.
     */
    public List<Map<String, Object>> getTransactionsByAccount(Long accountNumber) {
        return transactionsFeignClient.getTransactionsByAccount(accountNumber);
    }

    /**
     * Fetch all accounts from the Accounts microservice.
     */
    public List<Map<String, Object>> getAllAccounts() {
        return accountsFeignClient.getAllAccounts();
    }

    /**
     * Fetch login history from Customer service via Feign client.
     */
//    public List<Map<String,Object>> getCustomerLoginHistory(Long customerId, String from, String to) {
//        try {
//            return customerFeignClient.getLoginHistory(customerId, from, to);
//        } catch (Exception ex) {
//            return Collections.emptyList();
//        }
//    }
}

