package com.project.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.AccountBalanceResponseDto;
import com.project.dto.AccountCountsDto;
import com.project.dto.AccountRequestDto;
import com.project.dto.AccountResponseDto;
import com.project.dto.AccountStatusChangeDto;
import com.project.dto.BalanceUpdateDto;
import com.project.dto.TransactionRequestDto;
import com.project.dto.TransactionResponseDto;
import com.project.model.Account;
import com.project.model.AccountStatus;
import com.project.model.Branch;
import com.project.repository.AccountRepository;
import com.project.repository.BranchRepository;
import com.project.service.AccountService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BranchRepository branchRepository;

// 1. Create a new account
	@PostMapping("/createAccount")
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
	
	@GetMapping("/customers/{customerId}/transactions")
	public ResponseEntity<List<TransactionResponseDto>> getTransactionsByCustomerId(@PathVariable Long customerId) {
		List<TransactionResponseDto> accounts = accountService.getTransactionsByCustomerId(customerId);
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
	public ResponseEntity<AccountBalanceResponseDto> updateBalance(@PathVariable Long accountId,
			@RequestBody BalanceUpdateDto request) {
		AccountBalanceResponseDto response = accountService.updateBalance(accountId, request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/createTransaction")
	public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody TransactionRequestDto dto) {
		ResponseEntity<TransactionResponseDto> response = accountService.createTransaction(dto);

		if (response == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
	}

	@GetMapping("/admin/counts")
	public ResponseEntity<AccountCountsDto> getAdminCounts() {
		long total = accountRepository.count();
		long active = accountRepository.countByStatus(AccountStatus.ACTIVE);
		return ResponseEntity.ok(new AccountCountsDto(total, active));
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Account>> getAllAccounts() {
		List<Account> b = accountRepository.findAll();
		return ResponseEntity.ok(b);
	}

// Change account status (used by Admin)
	@PatchMapping("/{accountId}/status")
	public ResponseEntity<AccountResponseDto> updateAccountStatus(@PathVariable Long accountId,
			@RequestBody AccountStatusChangeDto req) {
		AccountResponseDto resp = accountService.updateAccountStatus(accountId, req);
		return ResponseEntity.ok(resp);
	}

	@GetMapping("/getAllBranches")
	public ResponseEntity<List<Branch>> getAllBranches() {
		List<Branch> b = branchRepository.findAll();
		return ResponseEntity.ok(b);

	}
	
	@PostMapping("/addBranch")
    public ResponseEntity<Branch> addBranch(@RequestBody Branch branch) {
        Branch saved = branchRepository.save(branch);
        return ResponseEntity.status(201).body(saved);
    }
	
	 @Autowired
	    private AccountService service;

	    @PutMapping("/deactivate/customer/{customerId}")
	    public boolean deactivateAccountsByCustomer(@PathVariable Long customerId) {
	        boolean b= service.deactivateAccountsByCustomer(customerId);
//	        return ResponseEntity.ok("Accounts for customer " + customerId + " deactivated");
	        return b;
	    }

	   
	

}