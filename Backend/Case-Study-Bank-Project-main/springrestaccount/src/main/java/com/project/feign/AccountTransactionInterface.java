package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.dto.TransactionResponseDto;



@FeignClient("project_transaction")
public interface AccountTransactionInterface {
	 @PostMapping("/create")
	    public ResponseEntity<String> createTransaction(
	             @RequestBody TransactionResponseDto dto);
}
