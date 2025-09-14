package com.admin.dto;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class AccountCountsDto {
    private long totalAccounts;
    private long activeAccounts;
}