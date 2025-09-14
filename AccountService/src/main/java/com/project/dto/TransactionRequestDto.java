package com.project.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
public class TransactionRequestDto {
	 private String fromAccountNumber;
	    private String toAccountNumber;
	    private BigDecimal amount;
	    private String fromEmail;
	    private String toEmail;
	    private String type;
	    private String description;
	    private BigDecimal balanceFrom;
	    private BigDecimal balanceTo;
	    private String pin;
}
