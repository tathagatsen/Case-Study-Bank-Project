package com.project.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.project.dto.AccountBalanceResponseDto;
import com.project.dto.AccountCreatedDto;
import com.project.dto.AccountRequestDto;
import com.project.dto.AccountResponseDto;
import com.project.dto.AccountStatusChangeDto;
import com.project.dto.BalanceUpdateDto;
import com.project.dto.EmailDTO;
import com.project.dto.TransactionRequestDto;
import com.project.dto.TransactionResponseDto;
import com.project.feign.AccountAdminClientInterface;
import com.project.feign.AccountTransactionInterface;
import com.project.feign.NotificationClientInterface;
import com.project.model.Account;
import com.project.model.AccountStatus;
import com.project.model.AccountType;
import com.project.model.Branch;
import com.project.repository.AccountRepository;
import com.project.repository.BranchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;

	@Autowired
	private AccountTransactionInterface accountTransactionInterface;

	@Autowired
	private AccountAdminClientInterface accountAdminClient;

	@Autowired
	private NotificationClientInterface notificationClient;
	
	@Autowired
	private BranchRepository branchRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	    public AccountResponseDto createAccount(AccountRequestDto request) {
	        AccountType type = parseAccountType(request.getAccountType());

	        // ✅ Find IFSC from branch table
	        Optional<Branch> branch = branchRepository.findByBranchId(request.getBranchId());
//	                .orElseThrow(() -> new ResourceNotFound("Branch not found: " + request.getBranchName()));

	        // ✅ Validate PIN
	        if (request.getPin() == null || !request.getPin().matches("\\d{4}")) {
	            throw new IllegalArgumentException("PIN must be a 4-digit number");
	        }

	        Account account = Account.builder()
	                .accountNumber(generateAccountNumber())
	                .customerId(request.getCustomerId())
	                .accountType(type)
	                .status(AccountStatus.ACTIVE)
	                .createdAt(LocalDateTime.now())
	                .updatedAt(LocalDateTime.now())
	                .email(request.getEmail())
	                .balance(BigDecimal.ZERO)
	                .pin(passwordEncoder.encode(request.getPin()))             // ✅ set pin
	                .ifscCode(branch.get().getIfscCode())    // ✅ set IFSC from branch
	                .branchName(branch.get().getBranchName())
	                .build();

	        Account saved = accountRepository.save(account);

	        // prepare response
	        AccountResponseDto response = accountMapper.toResponse(saved);
	        response.setIfscCode(branch.get().getIfscCode());
	        response.setBranchName(branch.get().getBranchName());

	        // ✅ notify admin
	        
	        AccountCreatedDto adminDto = AccountCreatedDto.builder()
	                .accountId(saved.getAccountId())
	                .accountNumber(saved.getAccountNumber())
	                .customerId(saved.getCustomerId())
	                .accountType(saved.getAccountType().name())
	                .status(saved.getStatus().name())
	                .balance(saved.getBalance())
	                .email(saved.getEmail())
	                .createdAt(saved.getCreatedAt())
	                .build();

//	        try {
//	            accountAdminClient.notifyAccountCreated(adminDto);
//	        } catch (Exception ex) {
//	            throw new RuntimeException("Failed to notify admin about account creation");
//	        }

	        // ✅ send notification
	        try {
	            EmailDTO email = new EmailDTO();
	            email.setTo(saved.getEmail());
	            email.setSubject("Your account " + saved.getAccountNumber() + " has been created");
	            email.setText("Dear Customer,\n\nYour account has been created successfully.\n" +
	                          "Account number: " + saved.getAccountNumber() + "\n" +
	                          "IFSC Code: " + branch.get().getIfscCode() + "\n\n" +
	                          "Regards,\nBank Team");
	            notificationClient.sendEmail(email);
	        } catch (Exception e) {
	            System.err.println("Failed to send notification email: " + e.getMessage());
	        }

	        return response;
	    }
	
	public AccountResponseDto getAccountById(Long accountId) {
		Account account = accountRepository.findById(accountId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with ID: " + accountId));

		try {
			EmailDTO email = new EmailDTO();
			email.setTo(account.getEmail());
			email.setSubject("Account details fetched");
			email.setText("Dear Customer,\n\nYour account status is currently: " + account.getStatus().name()
					+ ".\n\nRegards,\nBank Team");
			notificationClient.sendEmail(email);
		} catch (Exception e) {
			System.err.println("Failed to send account status email: " + e.getMessage());
		}

		return accountMapper.toResponse(account);
	}

	public AccountResponseDto getByAccountNumber(String accountNumber) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Account not found with number: " + accountNumber));
		return accountMapper.toResponse(account);
	}

	public List<AccountResponseDto> getAccountsByCustomerId(Long customerId) {
		List<Account> accounts = accountRepository.findByCustomerId(customerId);
		return accounts.stream().map(accountMapper::toResponse).collect(Collectors.toList());
	}

		public List<TransactionResponseDto> getTransactionsByCustomerId(Long customerId) {
		    List<Account> accounts = accountRepository.findByCustomerId(customerId);
	
		    // To store unique transactions
		    Map<Long, TransactionResponseDto> uniqueTransactions = new HashMap<>();
		    System.out.println(accounts + "ACCOUNTS");
		    for (Account account : accounts) {
		        ResponseEntity<List<TransactionResponseDto>> response =
		                transactionClient.getTransactionsForAccount(account.getAccountNumber());
		        System.out.println("TRXNS" + response);
		        if (response.getBody() != null) {
		            for (TransactionResponseDto tx : response.getBody()) {
		                // ✅ Deduplicate by transactionId
		                uniqueTransactions.put(tx.getTransactionId(), tx);
		            }
		        }
		    }
		    System.out.println(uniqueTransactions + "UYJIQ");
		    return new ArrayList(uniqueTransactions.values());
		}

	
	public AccountBalanceResponseDto getAccountBalance(Long accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
		BigDecimal balance = nz(account.getBalance());

		return AccountBalanceResponseDto.builder().balance(balance).build();
	}

	@Transactional
	public AccountBalanceResponseDto updateBalance(Long accountId, BalanceUpdateDto request) {
		if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive.");
		}
		if (request.getOperation() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operation is required (CREDIT or DEBIT).");
		}

		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

		if (account.getStatus() != AccountStatus.ACTIVE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update balance. Account is not active.");
		}

		String operation = request.getOperation().toUpperCase().trim();
		BigDecimal amount = request.getAmount();
		BigDecimal current = nz(account.getBalance());
		BigDecimal newBalance;

		switch (operation) {
		case "CREDIT":
			newBalance = current.add(amount);
			break;
		case "DEBIT":
			if (current.compareTo(amount) < 0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance for debit.");
			}
			newBalance = current.subtract(amount);
			break;
		default:
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation. Use 'CREDIT' or 'DEBIT'.");
		}

		account.setBalance(newBalance);
		account.setUpdatedAt(LocalDateTime.now());
		accountRepository.save(account);

		return AccountBalanceResponseDto.builder().balance(newBalance).build();
	}

	// -------- utils --------

	private AccountType parseAccountType(String type) {
		try {
			return AccountType.valueOf(type.toUpperCase());
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid account type. Use SAVINGS, SALARY, CURRENT, FD.");
		}
	}

	private String generateAccountNumber() {
		// Simple placeholder; replace with a robust generator in prod
		return "ACCT" + System.currentTimeMillis();
	}

	private BigDecimal nz(BigDecimal v) {
		return v == null ? BigDecimal.ZERO : v;
	}

	// change return type to ResponseEntity<TransactionResponseDto>
	public ResponseEntity<TransactionResponseDto> createTransaction(TransactionRequestDto dto) {
		Account accountFrom = accountRepository.findByAccountNumber(dto.getFromAccountNumber())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "From account not found"));
		Account accountTo = accountRepository.findByAccountNumber(dto.getToAccountNumber())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "To account not found"));

		// ✅ correct email setting
		dto.setFromEmail(accountFrom.getEmail());
		dto.setToEmail(accountTo.getEmail());
		dto.setBalanceFrom(accountFrom.getBalance());
		dto.setBalanceTo(accountTo.getBalance());
		
		if(!passwordEncoder.matches(dto.getPin(), accountFrom.getPin())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Incorrect Pin");
		}
		// ✅ ensure sufficient balance before calling transaction service
		if (accountFrom.getBalance().compareTo(dto.getAmount()) >= 0) {
			// forward to Transaction microservice via Feign
			TransactionResponseDto response = accountTransactionInterface.createTransaction(dto).getBody();
			accountFrom.setBalance(response.getBalanceFrom());
			accountTo.setBalance(response.getBalanceTo());
			accountFrom.setUpdatedAt(LocalDateTime.now());
			accountTo.setUpdatedAt(LocalDateTime.now());
			accountRepository.save(accountFrom);
			accountRepository.save(accountTo);

			// ✅ return response
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Insufficient balance in account: " + dto.getFromAccountNumber());
		}
	}

	// inject feign client

	@Transactional
	public AccountResponseDto updateAccountStatus(Long accountId, AccountStatusChangeDto changeDto) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
		
		String oldStatus = account.getStatus().name();
		account.setStatus(AccountStatus.valueOf(changeDto.getNewStatus()));
		account.setUpdatedAt(LocalDateTime.now());
		accountRepository.save(account);

		changeDto.setAccountId(accountId);
		changeDto.setCustomerId(account.getCustomerId());
		changeDto.setOldStatus(oldStatus);
		changeDto.setChangedAt(LocalDateTime.now());

		try {
			accountAdminClient.notifyAccountStatusChange(changeDto);
		} catch (Exception ex) {
			throw new RuntimeException("Failed to notify admin about account status change", ex);
		}

		return accountMapper.toResponse(account);
	}
	
	 @Autowired
	    private AccountRepository repo;

	    @Autowired
	    private AccountTransactionInterface transactionClient;

	    public boolean deactivateAccountsByCustomer(Long customerId) {
	        List<Account> accounts = repo.findByCustomerId(customerId);
	        for (Account acc : accounts) {
	            acc.setStatus(AccountStatus.SUSPENDED); // or INACTIVE
	            repo.save(acc);
	    
	            // cascade → freeze transactions
//	            transactionClient.freezeTransactionsByAccount(acc.getAccountNumber());
	        }
	        return true;
	    }

		

}
