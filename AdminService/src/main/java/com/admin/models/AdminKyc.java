package com.admin.models;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminKyc {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kycId;         // reference to Customer service KycDocument.id
    private Long customerId;
    private String documentType;
    private String filePath;
    private String status;      // PENDING / APPROVED / REJECTED
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private String remarks;
    private LocalDateTime createdAt;
}
