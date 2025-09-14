package com.admin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionAlertDTO {
    private String alertId;
    private String custId;
    private String transactionId;
    private String alertType; // FRAUD, LARGE_WITHDRAWAL
    private String description;
    private LocalDateTime generatedAt;
}
