package com.admin.dto;
import java.util.List;

import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminOverviewDto {
    private long totalUsers;
    private long activeUsers;
    private long totalAccounts;
    private long activeAccounts;
    private List<DailyTransactionCountDto> recentDailyTransactions;
}