package com.ofss.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TicketDto {
	
	private long ticketId;
	private String emailId;
	
	private Operation operation;
	public enum Operation{
		OPEN,
		IN_PROGRESS,
		RESOLVED,
		CLOSED
	}
	
	private String subject;
	
	
}
