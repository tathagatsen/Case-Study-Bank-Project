package com.project.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class UserCustomerDto {
	private Long customerId; // comes from Auth/User service

    private String name;
    private String email;
    private String phone;
    private String address;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
}
