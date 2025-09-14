package com.project.service.impl;

import com.project.dto.BalanceHistoryDto;
import com.project.dto.DailyTransactionCountDto;
import com.project.dto.FlagActionDto;
import com.project.dto.NotificationTransactionDto;
import com.project.dto.TransactionFlagDto;
import com.project.dto.TransactionRequestDto;
import com.project.dto.TransactionResponseDto;
import com.project.feign.AdminClientInterface;
import com.project.feign.TransactionNotificationInterface;
//import com.project.model.Account;
import com.project.model.Transaction;
import com.project.model.TransactionStatus;
import com.project.model.TransactionType;
import com.project.repository.DailyTransactionCountProjection;
//import com.project.repository.AccountRepository;
import com.project.repository.TransactionRepository;
import com.project.service.BalanceHistoryService;
import com.project.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionNotificationInterface transactionNotificationInterface;

	@Autowired
	private AdminClientInterface adminClient; // Feign to Admin to notify about flagged txns

	private final TransactionRepository transactionRepository;
	private final BalanceHistoryService balanceHistoryService;
//    private final AccountRepository accountRepository;

	public TransactionServiceImpl(TransactionRepository transactionRepository,
			BalanceHistoryService balanceHistoryService) {
		this.transactionRepository = transactionRepository;
		this.balanceHistoryService = balanceHistoryService;
//        this.accountRepository = accountRepository;
	}

	@Override
	@Transactional
	public TransactionResponseDto createTransaction(TransactionRequestDto request) {
		Transaction transaction = new Transaction();
		NotificationTransactionDto nDto = new NotificationTransactionDto();

		// ✅ Always initialize to avoid nulls
		BigDecimal fromAccountBalance = request.getBalanceFrom() != null ? request.getBalanceFrom() : BigDecimal.ZERO;
		BigDecimal toAccountBalance = request.getBalanceTo() != null ? request.getBalanceTo() : BigDecimal.ZERO;

		transaction.setFromAccountNumber(request.getFromAccountNumber());
		transaction.setToAccountNumber(request.getToAccountNumber());
		transaction.setAmount(request.getAmount());
		transaction.setType(TransactionType.valueOf(request.getType().toUpperCase()));
		transaction.setCreatedAt(LocalDateTime.now());

		BigDecimal amount = request.getAmount();

		try {
			switch (transaction.getType()) {
			case TRANSFER:
				if (request.getFromAccountNumber() == null || request.getToAccountNumber() == null) {
					throw new RuntimeException("Both from and to accounts are required for transfer");
				}
				if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
					throw new RuntimeException("From and To accounts cannot be the same for transfer");
				}

				if (fromAccountBalance.compareTo(amount) < 0) {
					throw new RuntimeException("Insufficient balance in account: " + request.getFromAccountNumber());
				}

				fromAccountBalance = fromAccountBalance.subtract(amount);
				toAccountBalance = toAccountBalance.add(amount);
				break;

			case DEPOSIT:
				if (request.getToAccountNumber() == null) {
					throw new RuntimeException("To account is required for deposit");
				}
				toAccountBalance = toAccountBalance.add(amount);
				// self-account
				transaction.setFromAccountNumber(request.getToAccountNumber());
				break;

			case WITHDRAWAL:
				if (request.getFromAccountNumber() == null) {
					throw new RuntimeException("From account is required for withdrawal");
				}
				if (fromAccountBalance.compareTo(amount) < 0) {
					throw new RuntimeException("Insufficient balance in account: " + request.getFromAccountNumber());
				}
				fromAccountBalance = fromAccountBalance.subtract(amount);
				// self-account
				transaction.setToAccountNumber(null);
				break;

			default:
				throw new RuntimeException("Unsupported transaction type: " + request.getType());
			}

			transaction.setStatus(TransactionStatus.SUCCESS);
			transaction.setDescription(request.getDescription());

			// ✅ prepare notification DTO
			nDto.setAmount(amount);
			nDto.setFromAccountId(request.getFromAccountNumber());
			nDto.setFromEmail(request.getFromEmail());
			nDto.setStatus(TransactionStatus.SUCCESS);
			nDto.setToAccountId(request.getToAccountNumber());
			nDto.setToEmail(request.getToEmail());

			try {
				transactionNotificationInterface.transactionStatus(nDto);
			} catch (Exception e) {
				// log only
			}

		} catch (RuntimeException ex) {
			transaction.setStatus(TransactionStatus.FAILED);
			transaction.setDescription(ex.getMessage());

			nDto.setAmount(amount);
			nDto.setFromAccountId(request.getFromAccountNumber());
			nDto.setFromEmail(request.getFromEmail());
			nDto.setStatus(TransactionStatus.FAILED);
			nDto.setToAccountId(request.getToAccountNumber());
			nDto.setToEmail(request.getToEmail());

			try {
				transactionNotificationInterface.transactionStatus(nDto);
			} catch (Exception e) {
				// log only
			}
		}

		Transaction saved = transactionRepository.save(transaction);

		// ✅ return updated balances in TransactionResponseDto
		TransactionResponseDto response = mapToResponseDto(saved);
		response.setBalanceFrom(fromAccountBalance);
		response.setBalanceTo(toAccountBalance);
		response.setFromEmail(request.getFromEmail());
		response.setToEmail(request.getToEmail());

		return response;
	}

	private void saveBalanceSnapshot(String accountId, BigDecimal balance) {
		BalanceHistoryDto snapshot = new BalanceHistoryDto();
		snapshot.setAccountId(accountId);
		snapshot.setBalance(balance);
		snapshot.setCreatedAt(LocalDateTime.now());
		balanceHistoryService.saveSnapshot(snapshot);
	}

	@Override
	public TransactionResponseDto getTransactionById(Long transactionId) {
		Transaction transaction = transactionRepository.findById(transactionId)
				.orElseThrow(() -> new RuntimeException("Transaction not found with id " + transactionId));
		return mapToResponseDto(transaction);
	}

	@Override
	public List<TransactionResponseDto> getTransactionsForAccount(String accountId) {
		return transactionRepository.findByFromAccountNumberOrToAccountNumber(accountId, accountId).stream()
				.map(this::mapToResponseDto).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponseDto> getMiniStatement(String accountId, int limit) {
		return transactionRepository.findLatestTransactions(accountId, PageRequest.of(0, limit)).stream()
				.map(this::mapToResponseDto).collect(Collectors.toList());
	}

	@Override
	public TransactionResponseDto reverseTransaction(Long transactionId, String reason) {
		Transaction transaction = transactionRepository.findById(transactionId)
				.orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));

		transaction.setStatus(TransactionStatus.REVERSED);
		transaction.setDescription(transaction.getDescription() + " | Reversed: " + reason);

		Transaction updated = transactionRepository.save(transaction);
		return mapToResponseDto(updated);
	}

	private TransactionResponseDto mapToResponseDto(Transaction transaction) {
		return TransactionResponseDto.builder().transactionId(transaction.getId())
				.fromAccountNumber(transaction.getFromAccountNumber()).toAccountNumber(transaction.getToAccountNumber())
				.amount(transaction.getAmount()).type(transaction.getType()).status(transaction.getStatus())
				.description(transaction.getDescription()).createdAt(transaction.getCreatedAt()).build();
	}

	@Transactional
	public void flagTransaction(Long txnId, FlagActionDto action) {
		Transaction tx = transactionRepository.findById(txnId)
				.orElseThrow(() -> new RuntimeException("Transaction not found " + txnId));
		if (tx.isFlagged())
			return; // idempotent

		tx.setFlagged(true);
		tx.setFlagReason(action.getRemarks());
		tx.setFlaggedBy(String.valueOf(action.getAdminId()));
		tx.setFlaggedAt(LocalDateTime.now());
		transactionRepository.save(tx);

		// notify admin
		TransactionFlagDto dto = TransactionFlagDto.builder().transactionId(tx.getId())
				.fromAccountNumber(tx.getFromAccountNumber()).toAccountNumber(tx.getToAccountNumber())
				.amount(tx.getAmount()).flagged(true).reason(tx.getFlagReason()).flaggedBy(tx.getFlaggedBy())
				.flaggedAt(tx.getFlaggedAt()).build();
//		try {
//			adminClient.notifyFlaggedTransaction(dto);
//		} catch (Exception e) {
//			throw new RuntimeException("admin notify failed", e);
//		}
	}

	@Transactional
	public void unflagTransaction(Long txnId, FlagActionDto action) {
		Transaction tx = transactionRepository.findById(txnId)
				.orElseThrow(() -> new RuntimeException("Transaction not found " + txnId));
		if (!tx.isFlagged())
			return; // idempotent

		tx.setFlagged(false);
		tx.setFlagReason(null);
		tx.setFlaggedAt(null);
		tx.setFlaggedBy(null);
		transactionRepository.save(tx);

		TransactionFlagDto dto = TransactionFlagDto.builder().transactionId(tx.getId()).flagged(false)
				.reason(action.getRemarks()).flaggedBy(String.valueOf(action.getAdminId()))
				.flaggedAt(LocalDateTime.now()).build();
//		try {
//			adminClient.notifyUnflaggedTransaction(dto);
//		} catch (Exception e) {
//			throw new RuntimeException("admin notify failed", e);
//		}
	}

	public List<TransactionFlagDto> listFlaggedTransactions() {
		return transactionRepository.findByFlaggedTrueOrderByCreatedAtDesc().stream()
				.map(tx -> TransactionFlagDto.builder().transactionId(tx.getId())
						.fromAccountNumber(tx.getFromAccountNumber()).toAccountNumber(tx.getToAccountNumber())
						.amount(tx.getAmount()).flagged(true).reason(tx.getFlagReason()).flaggedBy(tx.getFlaggedBy())
						.flaggedAt(tx.getFlaggedAt()).build())
				.collect(Collectors.toList());
	}

	public List<DailyTransactionCountProjection> getDailyCounts(LocalDateTime from, LocalDateTime to) {
		return transactionRepository.findDailyCounts(from, to);
	}
	
//	public List<TransactionResponseDto> getTransactionPeriodDetails(String accountNumber, String to, String from) {
//	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//	    LocalDateTime toDate = LocalDateTime.parse(to, formatter);
//	    LocalDateTime fromDate = LocalDateTime.parse(from, formatter);
//
//	    List<Transaction> transactions = transactionRepository.findAllByAccountNumberAndDateRange(accountNumber, fromDate, toDate);
//
//	    return transactions.stream()
//	            .map(this::mapToDto) // assume you have a mapper
//	            .toList();
//	}
	
	private TransactionResponseDto mapToDto(Transaction transaction) {
	    TransactionResponseDto dto = new TransactionResponseDto();
	    dto.setTransactionId(transaction.getId());
	    dto.setFromAccountNumber(transaction.getFromAccountNumber());
	    dto.setToAccountNumber(transaction.getToAccountNumber());
	    dto.setAmount(transaction.getAmount());
	    dto.setType(transaction.getType());
	    dto.setStatus(transaction.getStatus());
	    dto.setDescription(transaction.getDescription());
	    dto.setCreatedAt(transaction.getCreatedAt());
//	    dto.setFlagged(transaction.isFlagged());
//	    dto.setFlagReason(transaction.getFlagReason());
//	    dto.setFlaggedBy(transaction.getFlaggedBy());
//	    dto.setFlaggedAt(transaction.getFlaggedAt());
	    return dto;
	}


}

/*
 * package com.proje ct.service.impl;
 * 
 * import com.project.dto.BalanceHistoryDto; import
 * com.project.dto.TransactionRequestDto; import
 * com.project.dto.TransactionResponseDto; import com.project.model.Account;
 * import com.project.model.Transaction; import
 * com.project.model.TransactionStatus; import
 * com.project.model.TransactionType; import
 * com.project.repository.AccountRepository; import
 * com.project.repository.TransactionRepository; import
 * com.project.service.BalanceHistoryService; import
 * com.project.service.TransactionService; import
 * org.springframework.data.domain.PageRequest; import
 * org.springframework.stereotype.Service; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import java.math.BigDecimal; import java.time.LocalDateTime; import
 * java.util.List; import java.util.stream.Collectors;
 * 
 * @Service public class TransactionServiceImpl implements TransactionService {
 * 
 * private final TransactionRepository transactionRepository; private final
 * BalanceHistoryService balanceHistoryService; private final AccountRepository
 * accountRepository;
 * 
 * public TransactionServiceImpl(TransactionRepository transactionRepository,
 * BalanceHistoryService balanceHistoryService, AccountRepository
 * accountRepository) { this.transactionRepository = transactionRepository;
 * this.balanceHistoryService = balanceHistoryService; this.accountRepository =
 * accountRepository; }
 * 
 * @Override
 * 
 * @Transactional public TransactionResponseDto
 * createTransaction(TransactionRequestDto request) { Transaction transaction =
 * new Transaction(); transaction.setFromAccountId(request.getFromAccountId());
 * transaction.setToAccountId(request.getToAccountId());
 * transaction.setAmount(request.getAmount());
 * 
 * // Always set type
 * transaction.setType(TransactionType.valueOf(request.getType().toUpperCase()))
 * ; transaction.setCreatedAt(LocalDateTime.now());
 * 
 * try { // Fetch accounts Account fromAccount = null; Account toAccount = null;
 * 
 * if (request.getFromAccountId() != null) { fromAccount =
 * accountRepository.findById(request.getFromAccountId()) .orElseThrow(() -> new
 * RuntimeException("From account not found")); }
 * 
 * if (request.getToAccountId() != null) { toAccount =
 * accountRepository.findById(request.getToAccountId()) .orElseThrow(() -> new
 * RuntimeException("To account not found")); }
 * 
 * BigDecimal amount = request.getAmount();
 * 
 * // Check sufficient balance for debit/transfer/withdrawal if
 * ((transaction.getType() == TransactionType.TRANSFER || transaction.getType()
 * == TransactionType.WITHDRAWAL) && fromAccount.getBalance().compareTo(amount)
 * < 0) { throw new RuntimeException("Insufficient balance in account: " +
 * request.getFromAccountId()); }
 * 
 * // Perform transaction switch (transaction.getType()) { case TRANSFER: //
 * Deduct from sender
 * fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
 * accountRepository.save(fromAccount); saveBalanceSnapshot(fromAccount.getId(),
 * fromAccount.getBalance());
 * 
 * // Credit to receiver if (toAccount != null) {
 * toAccount.setBalance(toAccount.getBalance().add(amount));
 * accountRepository.save(toAccount); saveBalanceSnapshot(toAccount.getId(),
 * toAccount.getBalance()); } break;
 * 
 * case DEPOSIT: if (toAccount == null) throw new
 * RuntimeException("To account is required for deposit");
 * toAccount.setBalance(toAccount.getBalance().add(amount));
 * accountRepository.save(toAccount); saveBalanceSnapshot(toAccount.getId(),
 * toAccount.getBalance()); break;
 * 
 * case WITHDRAWAL: if (fromAccount == null) throw new
 * RuntimeException("From account is required for withdrawal");
 * fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
 * accountRepository.save(fromAccount); saveBalanceSnapshot(fromAccount.getId(),
 * fromAccount.getBalance()); break;
 * 
 * default: throw new RuntimeException("Unsupported transaction type: " +
 * request.getType()); }
 * 
 * transaction.setStatus(TransactionStatus.SUCCESS);
 * transaction.setDescription(request.getDescription());
 * 
 * } catch (RuntimeException ex) {
 * transaction.setStatus(TransactionStatus.FAILED);
 * transaction.setDescription(ex.getMessage());
 * 
 * // Ensure type is still set if (transaction.getType() == null) {
 * transaction.setType(TransactionType.valueOf(request.getType().toUpperCase()))
 * ; } }
 * 
 * Transaction saved = transactionRepository.save(transaction); return
 * mapToResponseDto(saved); }
 * 
 * private void saveBalanceSnapshot(String accountId, BigDecimal balance) {
 * BalanceHistoryDto snapshot = new BalanceHistoryDto();
 * snapshot.setAccountId(accountId); snapshot.setBalance(balance);
 * snapshot.setCreatedAt(LocalDateTime.now());
 * balanceHistoryService.saveSnapshot(snapshot); }
 * 
 * @Override public TransactionResponseDto getTransactionById(Long
 * transactionId) { Transaction transaction =
 * transactionRepository.findById(transactionId) .orElseThrow(() -> new
 * RuntimeException("Transaction not found with id " + transactionId)); return
 * mapToResponseDto(transaction); }
 * 
 * @Override public List<TransactionResponseDto>
 * getTransactionsForAccount(String accountId) { return
 * transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId)
 * .stream() .map(this::mapToResponseDto) .collect(Collectors.toList()); }
 * 
 * @Override public List<TransactionResponseDto> getMiniStatement(String
 * accountId, int limit) { return
 * transactionRepository.findLatestTransactions(accountId, PageRequest.of(0,
 * limit)) .stream() .map(this::mapToResponseDto) .collect(Collectors.toList());
 * }
 * 
 * @Override public TransactionResponseDto reverseTransaction(Long
 * transactionId, String reason) { Transaction transaction =
 * transactionRepository.findById(transactionId) .orElseThrow(() -> new
 * RuntimeException("Transaction not found: " + transactionId));
 * 
 * transaction.setStatus(TransactionStatus.REVERSED);
 * transaction.setDescription(transaction.getDescription() + " | Reversed: " +
 * reason);
 * 
 * Transaction updated = transactionRepository.save(transaction); return
 * mapToResponseDto(updated); }
 * 
 * private TransactionResponseDto mapToResponseDto(Transaction transaction) {
 * return new TransactionResponseDto( transaction.getId(),
 * transaction.getFromAccountId(), transaction.getToAccountId(),
 * transaction.getAmount(), transaction.getType(), transaction.getStatus(),
 * transaction.getDescription(), transaction.getCreatedAt() ); } }
 * 
 */