package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TwoFADto {
	private String email;
	private Purpose purpose; // PASSWORD_RESET, EMAIL_VERIFY, etc.
	public enum Purpose{
	    	PASSWORD_RESET,
	    	EMAIL_VERIFY
	 } 
}
