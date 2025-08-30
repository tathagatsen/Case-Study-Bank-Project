package com.project.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomerKycDto {
	 private Long customerId;
	 private String documentType; // Aadhaar, PAN, Passport
	 private String filePath;     // local storage path of the PDF
}
