package com.project.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomerSupportTicketDto {
	private Long customerId;
	private String subject;     
    private String description;
}
