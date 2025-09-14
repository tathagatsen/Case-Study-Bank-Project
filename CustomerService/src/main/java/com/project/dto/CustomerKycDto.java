package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerKycDto {
	
	 private Long customerId;
	 private String documentType; // Aadhaar, PAN, Passport
	 private String filePath; // local storage path of the PDF
	 private byte[] data;
}
