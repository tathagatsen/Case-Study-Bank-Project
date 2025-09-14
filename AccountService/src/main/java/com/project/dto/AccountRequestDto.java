package com.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestDto {
	private Long customerId;
	private String accountType; // Example: "SAVINGS", "SALARY"
	private String email;
	private Long branchId;  // ✅ new
    private String pin;  
}