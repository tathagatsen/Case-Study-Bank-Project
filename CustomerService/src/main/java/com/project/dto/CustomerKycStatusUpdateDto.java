package com.project.dto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerKycStatusUpdateDto {
    private Long kycId;            // KycDocument.id
    private Long customerId;
    private String status;         // APPROVED / REJECTED
    private String reviewedBy;     // adminId or admin username
    private String remarks;
    private LocalDateTime reviewedAt;
}