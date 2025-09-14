package com.admin.dto;

import java.time.LocalDateTime;
import lombok.*;

//CustomerAdminKycDto (same shape as in Customer service)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAdminKycDto {
 private Long kycId;
 private Long customerId;
 private String documentType;
 private String filePath;
 private LocalDateTime uploadedAt;
}
