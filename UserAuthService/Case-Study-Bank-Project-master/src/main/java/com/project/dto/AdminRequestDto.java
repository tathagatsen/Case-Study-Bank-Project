package com.project.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequestDto {
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String email;
	private String password;
	private String role;
	private String phoneNumber;
	private String address;
}
