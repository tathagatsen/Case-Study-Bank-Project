package com.project.dto;

import java.time.LocalDate;

import com.project.model.Role;
import com.project.model.Role.RoleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RegisterDto {
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String email;
	private String password;
	private String role;
	private String phoneNumber;
//	public enum ROLE{
//		CUSTOMER,
//        ADMIN,
//        SUPPORT
//	}
}
