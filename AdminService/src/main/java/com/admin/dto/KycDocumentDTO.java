package com.admin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor	
@AllArgsConstructor
@Builder
public class KycDocumentDTO {
    private String docId;
    private String custId;
    private String docType; // Aadhaar, PAN, Passport
    private String status; // PENDING, APPROVED, REJECTED, NEED_MORE_INFO
    private LocalDateTime uploadedAt;
}
