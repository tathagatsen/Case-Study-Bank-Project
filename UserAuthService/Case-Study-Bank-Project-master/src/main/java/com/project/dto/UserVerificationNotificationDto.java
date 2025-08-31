package com.project.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserVerificationNotificationDto {
	private String email;
	private String otp;
}
