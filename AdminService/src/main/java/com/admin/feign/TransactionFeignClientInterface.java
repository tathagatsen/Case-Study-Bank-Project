package com.admin.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;
import com.admin.dto.DailyTransactionCountDto;
import com.admin.dto.FlagActionDto;
import com.admin.dto.TransactionFlagDto;

@FeignClient(name = "Transaction", url = "http://localhost:8084", path = "/transactions")
public interface TransactionFeignClientInterface {
	@PostMapping("/{txnId}/flag")
	public ResponseEntity<Void> flagTransaction(@PathVariable Long txnId, @RequestBody FlagActionDto action);

	@PostMapping("/{txnId}/unflag")
	public ResponseEntity<Void> unflagTransaction(@PathVariable Long txnId, @RequestBody FlagActionDto action);

	@GetMapping("/admin/flagged")
	public ResponseEntity<List<TransactionFlagDto>> getFlaggedTransactions();

	@GetMapping("/admin/daily-counts")
	public ResponseEntity<List<DailyTransactionCountDto>> getDailyCounts(@RequestParam String from, @RequestParam String to);


    @GetMapping("/{transactionId}")
    Map<String, Object> getTransactionById(@PathVariable("transactionId") Long transactionId);

    @GetMapping("/account/{accountNumber}")
    List<Map<String, Object>> getTransactionsByAccount(@PathVariable("accountNumber") Long accountNumber);
}
