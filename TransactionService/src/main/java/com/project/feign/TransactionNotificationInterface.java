package com.project.feign;

import java.util.List;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.project.dto.NotificationTransactionDto;
import com.project.dto.TransactionResponseDto;



@FeignClient(name = "02-NotificationsModule",url = "http://localhost:8085/notifications")
public interface TransactionNotificationInterface {
	@PostMapping("/transactionStatus")
	public boolean transactionStatus(@RequestBody NotificationTransactionDto dto);
	
	@GetMapping("/transactions/account/{accountNumber}")
	ResponseEntity<List<TransactionResponseDto>> getTransactionsForAccount(@PathVariable("accountNumber") String accountNumber);
}
