package com.project.dto;

import java.time.LocalDateTime;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountStatusChangeDto {
    private Long accountId;
    private Long customerId;
    private String oldStatus;
    private String newStatus;
    private Long changedByAdminId;
    private String reason;
    private LocalDateTime changedAt;
}