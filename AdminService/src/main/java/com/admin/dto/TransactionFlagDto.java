package com.admin.dto;

import java.time.LocalDateTime;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionFlagDto {
    private Long transactionId;
    private Long accountId;
    private Long customerId;
    private String reason;
    private String flaggedBy; // "SYSTEM" or admin username/id
    private LocalDateTime flaggedAt;
    private boolean flagged;
}