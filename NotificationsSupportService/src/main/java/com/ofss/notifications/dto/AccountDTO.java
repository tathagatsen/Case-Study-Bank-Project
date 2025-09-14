package com.ofss.notifications.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
	private long accountId;
	
	private String accountNumber;
	
	private String customerName;
	
	private BigDecimal balance;
	
	private String subject;
	
	public AccountStatus accountStatus;
	public enum AccountStatus {
	    ACTIVE,
	    SUSPENDED,
	    FROZEN,
	    CLOSED
	}
}
