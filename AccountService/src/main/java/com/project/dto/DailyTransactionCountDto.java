package com.project.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DailyTransactionCountDto {
    private LocalDate date;
    private Long count;
    private BigDecimal totalAmount;
}