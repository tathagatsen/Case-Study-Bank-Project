package com.project.service;

import com.project.dto.TransactionRequestDto;
import com.project.dto.TransactionResponseDto;
import java.util.List;

public interface TransactionService {
    TransactionResponseDto createTransaction(TransactionRequestDto request);
    TransactionResponseDto getTransactionById(Long transactionId);
    List<TransactionResponseDto> getTransactionsForAccount(String accountId);
    List<TransactionResponseDto> getMiniStatement(String accountId, int limit);
    TransactionResponseDto reverseTransaction(Long transactionId, String reason);
//	List<TransactionResponseDto> getTransactionPeriodDetails(String accountNumber, String to, String from);
}
