package com.ofss.notifications.dto;

import java.math.BigDecimal;

import com.ofss.notifications.model.TransactionStatus;

import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
public class NotificationTransactionDto {
	private String fromEmail;
	private String toEmail;
	private String fromAccountId;
	private String toAccountId;
	private BigDecimal amount;
	private TransactionStatus status;
}
