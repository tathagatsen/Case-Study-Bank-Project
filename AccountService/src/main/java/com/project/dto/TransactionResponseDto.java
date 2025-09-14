package com.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TransactionResponseDto {
	private Long transactionId;
	private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private String type;
    private String status;
    private String description;
    private LocalDateTime createdAt;
    private BigDecimal balanceFrom;
    private BigDecimal balanceTo;
    private String fromEmail;
    private String toEmail;

}
