package com.project.dto;

import lombok.Data;

@Data
public class PassWordUpdateDto {
	private String email;
	private String otp;
	private String newPassword;
}
