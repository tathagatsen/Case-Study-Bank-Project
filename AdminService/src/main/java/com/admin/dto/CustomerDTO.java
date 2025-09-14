package com.admin.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private String custId;
    private String name;
    private String email;
    private String status; // ACTIVE, PENDING_KYC, DEACTIVATED
}
