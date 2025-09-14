package com.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccountResponseDto {
	private Long accountId;
	private String accountNumber;
	private Long customerId;
	private String accountType;
	private String status;
	private BigDecimal balance;
	private String ifscCode;
	private String branchName;
	//private BigDecimal heldAmount;
	//private BigDecimal availableBalance;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
