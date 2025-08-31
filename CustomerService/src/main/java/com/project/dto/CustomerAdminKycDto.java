package com.project.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomerAdminKycDto {
	 private Long customerId;
	 private String documentType; // Aadhaar, PAN, Passport
	 private String filePath;   
}
