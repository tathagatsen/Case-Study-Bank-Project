package com.project.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAdminKycDto {
    private Long kycId;         // NEW: the KYC doc id in Customer DB
    private Long customerId;
    private String documentType;
    private String filePath;
    private LocalDateTime uploadedAt;
}