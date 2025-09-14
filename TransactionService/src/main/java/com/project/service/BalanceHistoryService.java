package com.project.service;

import com.project.dto.BalanceHistoryDto;
import java.util.List;

public interface BalanceHistoryService {
    BalanceHistoryDto saveSnapshot(BalanceHistoryDto dto);
    BalanceHistoryDto getLatest(String accountId);
    List<BalanceHistoryDto> getHistory(String accountId);
    List<BalanceHistoryDto> getMini(String accountId, int limit);
}
