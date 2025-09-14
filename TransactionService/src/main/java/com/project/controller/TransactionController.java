package com.project.controller;

import com.project.dto.DailyTransactionCountDto;
import com.project.dto.FlagActionDto;
import com.project.dto.TransactionFlagDto;
import com.project.dto.TransactionRequestDto;
import com.project.dto.TransactionResponseDto;
import com.project.model.Transaction;
import com.project.repository.DailyTransactionCountProjection;
import com.project.repository.TransactionRepository;
import com.project.service.TransactionService;
import com.project.service.impl.TransactionServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

	private final TransactionService transactionService;
	
	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private TransactionServiceImpl transactionServiceImpl;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping("/create")
	public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody TransactionRequestDto request) {
		return new ResponseEntity<>(transactionService.createTransaction(request), HttpStatus.CREATED);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
		return ResponseEntity.ok(transactionService.getTransactionById(id));
	}

	@GetMapping("/account/{accountNumber}")
	public ResponseEntity<List<TransactionResponseDto>> getTransactionsForAccount(@PathVariable String accountNumber) {
		return ResponseEntity.ok(transactionService.getTransactionsForAccount(accountNumber));
	}

	@GetMapping("/account/{accountId}/mini-statement")
	public ResponseEntity<List<TransactionResponseDto>> getMiniStatement(@PathVariable String accountId,
			@RequestParam(defaultValue = "5") int limit) {
		return ResponseEntity.ok(transactionService.getMiniStatement(accountId, limit));
	}

	@PostMapping("/{id}/flag")
	public ResponseEntity<Void> flagTransaction(@PathVariable Long id, @RequestBody FlagActionDto action) {
		transactionServiceImpl.flagTransaction(id, action);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/unflag")
	public ResponseEntity<Void> unflagTransaction(@PathVariable Long id, @RequestBody FlagActionDto action) {
		transactionServiceImpl.unflagTransaction(id, action);
		return ResponseEntity.ok().build();
	}

	// Admin usage
	@GetMapping("/admin/flagged")
	public ResponseEntity<List<TransactionFlagDto>> getFlaggedTransactions() {
		return ResponseEntity.ok(transactionServiceImpl.listFlaggedTransactions());
	}

	@GetMapping("/admin/daily-counts")
	public ResponseEntity<List<DailyTransactionCountProjection>> dailyCounts(@RequestParam String from,
			@RequestParam String to) {
		LocalDateTime f = LocalDate.parse(from).atStartOfDay();
		LocalDateTime t = LocalDate.parse(to).atTime(LocalTime.MAX); // 23:59:59.999999999 on 'to' date
		return ResponseEntity.ok(transactionServiceImpl.getDailyCounts(f, t));
	}

	@GetMapping("/all")
	public ResponseEntity<List<Transaction>> getAllTransaction() {
	    List<Transaction> transactions = transactionRepo.findAll();
	    return ResponseEntity.ok(transactions);
	}

//	@GetMapping("/getTransactionPeriodDetails/{accountNumber}")
//	List<TransactionResponseDto> getTransactionPeriodDetails(@PathVariable String accountNumber, String to,
//			String from) {
//		List<TransactionResponseDto> response=transactionService.getTransactionPeriodDetails(accountNumber,to,from);
//		return response;
//	}

}

/*
 * package com.project.controller;
 * 
 * import com.project.dto.TransactionRequestDto; import
 * com.project.dto.TransactionResponseDto; import
 * com.project.service.TransactionService; import jakarta.validation.Valid;
 * import org.springframework.http.HttpStatus; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import java.util.List;
 * 
 * @RestController
 * 
 * @RequestMapping("/transactions")
 * 
 * @CrossOrigin(origins = "*") public class TransactionController {
 * 
 * private final TransactionService transactionService;
 * 
 * // Constructor-based Dependency Injection public
 * TransactionController(TransactionService transactionService) {
 * this.transactionService = transactionService; }
 * 
 * // Create transaction //post http://localhost:8085/transactions/create
 * 
 * { "fromAccountId": 1001, "toAccountId": 1002, "amount": 5000.00, "type":
 * "TRANSFER", "status": "SUCCESS", "description": "Payment for invoice #123" }
 * 
 * 
 * 
 * @PostMapping("create") public ResponseEntity<TransactionResponseDto>
 * createTransaction(
 * 
 * @Valid @RequestBody TransactionRequestDto request) { TransactionResponseDto
 * response = transactionService.createTransaction(request); return new
 * ResponseEntity<>(response, HttpStatus.CREATED); }
 * 
 * // Get transaction by ID //http://localhost:8085/transactions/get/7
 * 
 * @GetMapping("/get/{id}") public ResponseEntity<TransactionResponseDto>
 * getTransactionById(@PathVariable("id") Long transactionId) {
 * TransactionResponseDto response =
 * transactionService.getTransactionById(transactionId); return
 * ResponseEntity.ok(response); }
 * 
 * // Get all transactions for an account
 * //http://localhost:8085/transactions/account/1001
 * 
 * @GetMapping("/account/{accountId}") public
 * ResponseEntity<List<TransactionResponseDto>>
 * getTransactionsForAccount(@PathVariable("accountId") Long accountId) {
 * List<TransactionResponseDto> response =
 * transactionService.getTransactionsForAccount(accountId); return
 * ResponseEntity.ok(response); }
 * 
 * // Get mini statement (limit)
 * //http://localhost:8085/transactions/account/1001/mini-statement
 * 
 * @GetMapping("/account/{accountId}/mini-statement") public
 * ResponseEntity<List<TransactionResponseDto>> getMiniStatement(
 * 
 * @PathVariable("accountId") Long accountId,
 * 
 * @RequestParam(defaultValue = "5") int limit) { List<TransactionResponseDto>
 * response = transactionService.getMiniStatement(accountId, limit); return
 * ResponseEntity.ok(response); } }
 */