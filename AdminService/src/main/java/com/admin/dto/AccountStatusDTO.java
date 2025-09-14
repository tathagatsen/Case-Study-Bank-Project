package com.admin.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatusDTO {
    private String accountId;
    private String custId;
    private String status; // ACTIVE, FROZEN, CLOSED
}
