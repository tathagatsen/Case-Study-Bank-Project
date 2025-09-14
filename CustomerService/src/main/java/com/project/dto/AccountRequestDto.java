package com.project.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccountRequestDto {
	private Long customerId;
	private String accountType; // Example: "SAVINGS", "SALARY"
	private String email;
	private long branchId;
	private String pin;
}
