package com.admin.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.dto.*;
import com.admin.feign.AccountFeignClientInterface;
import com.admin.feign.CustomerFeignClient;
import com.admin.feign.TransactionFeignClientInterface;
import com.admin.models.AdminActivityLog;
import com.admin.repositories.AdminActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AccountFeignClientInterface accountFeignClient;
    private final TransactionFeignClientInterface transactionFeignClient;
    private final CustomerFeignClient customerFeignClient;
    @Autowired
    private AdminActivityRepository activityRepo;

    public AdminOverviewDto getOverview(LocalDate from, LocalDate to) {
        CustomerCountDto users = customerFeignClient.getUserCounts();
        AccountCountsDto accounts = accountFeignClient.getCounts();
        List<DailyTransactionCountDto> daily = transactionFeignClient.getDailyCounts(from.toString(), to.toString()).getBody();
        return AdminOverviewDto.builder()
                .totalUsers(users.getTotalUsers())
                .activeUsers(users.getActiveUsers())
                .totalAccounts(accounts.getTotalAccounts())
                .activeAccounts(accounts.getActiveAccounts())
                .recentDailyTransactions(daily)
                .build();
    }

    public void flagTransactionAsAdmin(Long txnId, FlagActionDto action) {
        transactionFeignClient.flagTransaction(txnId, action);
        activityRepo.save(new AdminActivityLog(null, action.getAdminId(), "FLAG_TRANSACTION", "TRANSACTION", txnId, action.getRemarks(), LocalDateTime.now()));
    }

    public void unflagTransactionAsAdmin(Long txnId, FlagActionDto action) {
        transactionFeignClient.unflagTransaction(txnId, action);
        activityRepo.save(new AdminActivityLog(null, action.getAdminId(), "UNFLAG_TRANSACTION", "TRANSACTION", txnId, action.getRemarks(), LocalDateTime.now()));
    }

    public void changeAccountStatusAsAdmin(Long accountId, AccountStatusChangeDto dto) {
        accountFeignClient.updateStatus(accountId, dto);
        activityRepo.save(new AdminActivityLog(null, dto.getChangedByAdminId(), "ACCOUNT_STATUS_CHANGE", "ACCOUNT", accountId, dto.getReason(), LocalDateTime.now()));
    }
    
    public List<AdminActivityLog> getRecentActivities() {
        return activityRepo.findTop100ByOrderByTimestampDesc();
    }
}
