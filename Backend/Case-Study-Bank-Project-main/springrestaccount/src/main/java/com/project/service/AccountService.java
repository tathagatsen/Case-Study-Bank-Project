package com.project.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.project.dto.AccountBalanceResponseDto;
import com.project.dto.AccountRequestDto;
import com.project.dto.AccountResponseDto;
import com.project.dto.BalanceUpdateDto;
import com.project.dto.TransactionResponseDto;
import com.project.feign.AccountTransactionInterface;
import com.project.model.Account;
import com.project.model.AccountStatus;
import com.project.model.AccountType;
import com.project.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    
    @Autowired
    private AccountTransactionInterface accountTransactionInterface;
    public AccountResponseDto createAccount(AccountRequestDto request) {
//        if (request.getInitialDeposit() == null || request.getInitialDeposit().compareTo(BigDecimal.ZERO) < 0) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Initial deposit must be non-negative.");
//        }

        AccountType type = parseAccountType(request.getAccountType());

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .customerId(request.getCustomerId())
                .accountType(type)
                .status(AccountStatus.ACTIVE)
                .balance(null)
                .heldAmount(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Account saved = accountRepository.save(account);
        return accountMapper.toResponse(saved);
    }

    public AccountResponseDto getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with ID: " + accountId));
        return accountMapper.toResponse(account);
    }

    public AccountResponseDto getByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with number: " + accountNumber));
        return accountMapper.toResponse(account);
    }

    public List<AccountResponseDto> getAccountsByCustomerId(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        return accounts.stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
    }

    public AccountBalanceResponseDto getAccountBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        BigDecimal balance = nz(account.getBalance());
        BigDecimal held = nz(account.getHeldAmount());
        BigDecimal available = balance.subtract(held);

        return AccountBalanceResponseDto.builder()
                .balance(balance)
                .build();
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

        BigDecimal held = nz(account.getHeldAmount());
        BigDecimal available = newBalance.subtract(held);

        return AccountBalanceResponseDto.builder()
                .balance(newBalance)
                .build();
    }

    // -------- utils --------

    private AccountType parseAccountType(String type) {
        try {
            return AccountType.valueOf(type.toUpperCase());
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid account type. Use SAVINGS, SALARY, CURRENT, FD."
            );
        }
    }

    private String generateAccountNumber() {
        // Simple placeholder; replace with a robust generator in prod
        return "ACCT" + System.currentTimeMillis();
    }

    private BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

	public TransactionResponseDto createTransaction(TransactionResponseDto dto) {
		Account account = accountRepository.findByAccountNumber(dto.getFromAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
		dto.setEmail(account.getEmail());
		dto.setCustId(account.getCustomerId());
		dto.setFromAccountId(account.getAccountNumber());
		accountTransactionInterface.createTransaction(dto);
		return null;
	}
}
