package com.project.controller;

import com.project.dto.BalanceHistoryDto;
import com.project.service.BalanceHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balance-history")
public class BalanceHistoryController {

    private final BalanceHistoryService balanceHistoryService;

    public BalanceHistoryController(BalanceHistoryService balanceHistoryService) {
        this.balanceHistoryService = balanceHistoryService;
    }

    @PostMapping
    public ResponseEntity<BalanceHistoryDto> saveSnapshot(@RequestBody BalanceHistoryDto dto) {
        BalanceHistoryDto saved = balanceHistoryService.saveSnapshot(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/account/{accountId}/latest")
    public ResponseEntity<BalanceHistoryDto> getLatest(@PathVariable String accountId) {
        return ResponseEntity.ok(balanceHistoryService.getLatest(accountId));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<BalanceHistoryDto>> getHistory(@PathVariable String accountId) {
        return ResponseEntity.ok(balanceHistoryService.getHistory(accountId));
    }

    @GetMapping("/account/{accountId}/mini")
    public ResponseEntity<List<BalanceHistoryDto>> getMini(@PathVariable String accountId,
                                                           @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return ResponseEntity.ok(balanceHistoryService.getMini(accountId, limit));
    }
}
