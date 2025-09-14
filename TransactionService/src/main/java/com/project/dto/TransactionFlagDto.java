package com.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionFlagDto {
    private Long transactionId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private Long accountId; // optional
    private Long customerId; // optional
    private boolean flagged;
    private String reason;
    private String flaggedBy;
    private LocalDateTime flaggedAt;
}
