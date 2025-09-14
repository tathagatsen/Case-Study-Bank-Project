package com.ofss.notifications.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserOTPNotificationDto {
	private String email;
	private Long customerId;
	
}
