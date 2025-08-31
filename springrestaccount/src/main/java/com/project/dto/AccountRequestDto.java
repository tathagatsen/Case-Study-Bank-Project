package com.project.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestDto {
private Long customerId;
private String accountType; // Example: "SAVINGS", "SALARY"
private String email;
}