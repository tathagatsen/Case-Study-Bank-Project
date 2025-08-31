package com.project.service;

import org.springframework.stereotype.Component;

import com.project.dto.AccountResponseDto;
import com.project.model.Account;

@Component
public class AccountMapper {

public AccountResponseDto toResponse(Account account) {
    return AccountResponseDto.builder()
            .accountId(account.getAccountId())
            .accountNumber(account.getAccountNumber())
            .customerId(account.getCustomerId())
            .accountType(account.getAccountType().name())
            .status(account.getStatus().name())
            .balance(account.getBalance())
            .heldAmount(account.getHeldAmount())
            .availableBalance(account.getBalance().subtract(account.getHeldAmount()))
            .createdAt(account.getCreatedAt())
            .updatedAt(account.getUpdatedAt())
            .build();
}


}