package com.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class TransactionResponseDto {
    private String fromAccountId;
    private Long custId;
    private String email;

}
