package com.project.controller;

import java.security.PublicKey;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.AccountBalanceResponseDto;
import com.project.dto.AccountRequestDto;
import com.project.dto.AccountResponseDto;
import com.project.dto.BalanceUpdateDto;
import com.project.dto.TransactionResponseDto;
import com.project.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

// 1. Create a new account
@PostMapping
public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto request) {
    AccountResponseDto response = accountService.createAccount(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

// 2. Get account by ID
@GetMapping("/{accountId}")
public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable Long accountId) {
    AccountResponseDto response = accountService.getAccountById(accountId);
    return ResponseEntity.ok(response);
}

// 3. Get account by account number
@GetMapping("/number/{accountNumber}")
public ResponseEntity<AccountResponseDto> getByAccountNumber(@PathVariable String accountNumber) {
    AccountResponseDto response = accountService.getByAccountNumber(accountNumber);
    return ResponseEntity.ok(response);
}

// 4. List all accounts for a customer
@GetMapping("/customers/{customerId}/accounts")
public ResponseEntity<List<AccountResponseDto>> getAccountsByCustomerId(@PathVariable Long customerId) {
    List<AccountResponseDto> accounts = accountService.getAccountsByCustomerId(customerId);
    return ResponseEntity.ok(accounts);
}

// 5. Get balance info (balance + held + available)
@GetMapping("/{accountId}/balance")
public ResponseEntity<AccountBalanceResponseDto> getBalance(@PathVariable Long accountId) {
    AccountBalanceResponseDto response = accountService.getAccountBalance(accountId);
    return ResponseEntity.ok(response);
}

// 6. Update balance (used by Transaction Service)
@PatchMapping("/{accountId}/update-balance")
public ResponseEntity<AccountBalanceResponseDto> updateBalance(
        @PathVariable Long accountId,
        @RequestBody BalanceUpdateDto request) {
    AccountBalanceResponseDto response = accountService.updateBalance(accountId, request);
    return ResponseEntity.ok(response);
}

public TransactionResponseDto createTransaction(@RequestBody TransactionResponseDto dto){
	return accountService.createTransaction(dto);
	
}


}