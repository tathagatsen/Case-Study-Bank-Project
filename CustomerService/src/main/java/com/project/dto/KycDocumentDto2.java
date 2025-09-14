package com.project.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycDocumentDto2 {
private Long kycId;
private Long customerId;
private String documentType;
private String filePath;
private String status; // PENDING, APPROVED, REJECTED
private LocalDateTime uploadedAt;
private LocalDateTime reviewedAt;
private String reviewedBy;
private String remarks;
}
