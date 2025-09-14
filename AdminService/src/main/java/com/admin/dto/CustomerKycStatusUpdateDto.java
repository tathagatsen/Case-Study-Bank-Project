package com.admin.dto;

import java.time.LocalDateTime;
import lombok.*;

//CustomerKycStatusUpdateDto (to call back to Customer)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerKycStatusUpdateDto {
 private Long kycId;
 private Long customerId;
 private String status;    // APPROVED / REJECTED
 private String reviewedBy;
 private String remarks;
 private LocalDateTime reviewedAt;
}
