package com.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BalanceHistoryDto {
    private Long id;
    private String accountId; // String
    private BigDecimal balance;
    private LocalDateTime createdAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public BalanceHistoryDto() {
		super();
	}

    // getters & setters...
}
