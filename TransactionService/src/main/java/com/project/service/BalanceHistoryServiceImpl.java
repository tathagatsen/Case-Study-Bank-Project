
package com.project.service;

import com.project.dto.BalanceHistoryDto;
import com.project.model.BalanceHistory;
import com.project.repository.BalanceHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BalanceHistoryServiceImpl implements BalanceHistoryService {

    private final BalanceHistoryRepository balanceHistoryRepository;

    public BalanceHistoryServiceImpl(BalanceHistoryRepository balanceHistoryRepository) {
        this.balanceHistoryRepository = balanceHistoryRepository;
    }

    @Override
    public BalanceHistoryDto saveSnapshot(BalanceHistoryDto dto) {
        BalanceHistory entity = toEntity(dto);
        if (entity.getCreatedAt() == null) entity.setCreatedAt(LocalDateTime.now());
        BalanceHistory saved = balanceHistoryRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public BalanceHistoryDto getLatest(String accountId) {
        List<BalanceHistory> list = balanceHistoryRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
        return list.isEmpty() ? null : toDto(list.get(0));
    }

    @Override
    public List<BalanceHistoryDto> getHistory(String accountId) {
        return balanceHistoryRepository.findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BalanceHistoryDto> getMini(String accountId, int limit) {
        return balanceHistoryRepository.findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream().limit(limit).map(this::toDto).collect(Collectors.toList());
    }

    private BalanceHistoryDto toDto(BalanceHistory entity) {
        BalanceHistoryDto dto = new BalanceHistoryDto();
        dto.setId(entity.getId());
        dto.setAccountId(entity.getAccountId());
        dto.setBalance(entity.getBalance());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    private BalanceHistory toEntity(BalanceHistoryDto dto) {
        BalanceHistory entity = new BalanceHistory();
        entity.setId(dto.getId());
        entity.setAccountId(dto.getAccountId());
        entity.setBalance(dto.getBalance());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}

/*
 * package com.project.service;
 * 
 * import com.project.dto.BalanceHistoryDto; import
 * com.project.model.BalanceHistory; import
 * com.project.repository.BalanceHistoryRepository; import
 * org.springframework.stereotype.Service;
 * 
 * import java.time.LocalDateTime; import java.util.List; import
 * java.util.stream.Collectors;
 * 
 * @Service public class BalanceHistoryServiceImpl implements
 * BalanceHistoryService {
 * 
 * private final BalanceHistoryRepository balanceHistoryRepository;
 * 
 * // Constructor-based injection (no Lombok needed) public
 * BalanceHistoryServiceImpl(BalanceHistoryRepository balanceHistoryRepository)
 * { this.balanceHistoryRepository = balanceHistoryRepository; }
 * 
 * @Override public BalanceHistoryDto saveSnapshot(BalanceHistoryDto dto) {
 * BalanceHistory entity = toEntity(dto); if (entity.getCreatedAt() == null) {
 * entity.setCreatedAt(LocalDateTime.now()); } BalanceHistory saved =
 * balanceHistoryRepository.save(entity); return toDto(saved); }
 * 
 * @Override public BalanceHistoryDto getLatest(Long accountId) {
 * List<BalanceHistory> list =
 * balanceHistoryRepository.findByAccountIdOrderByCreatedAtDesc(accountId); if
 * (list.isEmpty()) { return null; } return toDto(list.get(0)); }
 * 
 * @Override public List<BalanceHistoryDto> getHistory(Long accountId) { return
 * balanceHistoryRepository.findByAccountIdOrderByCreatedAtDesc(accountId)
 * .stream() .map(this::toDto) .collect(Collectors.toList()); }
 * 
 * @Override public List<BalanceHistoryDto> getMini(Long accountId, int limit) {
 * List<BalanceHistory> list =
 * balanceHistoryRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
 * return list.stream() .limit(limit) .map(this::toDto)
 * .collect(Collectors.toList()); }
 * 
 * // ==================== Mapping Helpers ====================
 * 
 * private BalanceHistoryDto toDto(BalanceHistory entity) { BalanceHistoryDto
 * dto = new BalanceHistoryDto(); dto.setId(entity.getId());
 * dto.setAccountId(entity.getAccountId()); dto.setBalance(entity.getBalance());
 * dto.setCreatedAt(entity.getCreatedAt()); return dto; }
 * 
 * private BalanceHistory toEntity(BalanceHistoryDto dto) { BalanceHistory
 * entity = new BalanceHistory(); entity.setId(dto.getId());
 * entity.setAccountId(dto.getAccountId()); entity.setBalance(dto.getBalance());
 * entity.setCreatedAt(dto.getCreatedAt()); return entity; } }
 */