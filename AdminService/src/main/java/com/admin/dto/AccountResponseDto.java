package com.admin.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDto {
private Long accountId;
private String accountNumber;
private Long customerId;
private String accountType;
private String status;
private BigDecimal balance;
//private BigDecimal heldAmount;
//private BigDecimal availableBalance;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;
}