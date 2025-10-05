package com.project.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceUpdateDto {
private BigDecimal amount;
private String operation; // "CREDIT" or "DEBIT"
}
