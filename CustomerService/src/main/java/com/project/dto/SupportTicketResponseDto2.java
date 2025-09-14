package com.project.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SupportTicketResponseDto2 {
	private Long ticketId;
    private String subject;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private Long customerId;
}
