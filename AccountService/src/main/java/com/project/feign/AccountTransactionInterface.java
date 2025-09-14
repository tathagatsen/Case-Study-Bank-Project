package com.project.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.dto.TransactionRequestDto;
import com.project.dto.TransactionResponseDto;

@FeignClient(name = "Transaction", url = "http://localhost:8084")
public interface AccountTransactionInterface {
	@PostMapping("/transactions/create")
	ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody TransactionRequestDto dto);
	
	@GetMapping("/transactions/account/{accountNumber}")
	ResponseEntity<List<TransactionResponseDto>> getTransactionsForAccount(@PathVariable("accountNumber") String accountNumber);

	
	@PutMapping("/freeze/{accountNumber}")
    void freezeTransactionsByAccount(@PathVariable("accountNumber") String accountNumber);
	
	}
