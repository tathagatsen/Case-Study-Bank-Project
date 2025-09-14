package com.project.dto;
import lombok.*;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicketResponseDto {
	private Long ticketId;
    private String subject;
    private String description;
    private String status;      // OPEN, IN_PROGRESS, RESOLVED, CLOSED
    private LocalDateTime createdAt;
}
