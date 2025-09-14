package com.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountCreatedDto {
    private Long accountId;
    private String accountNumber;
    private Long customerId;
    private String accountType;
    private String status;
    private BigDecimal balance;
    private String email;
    private LocalDateTime createdAt;
}