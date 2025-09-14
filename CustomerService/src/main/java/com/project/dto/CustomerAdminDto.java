package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAdminDto {
	private Long id;
    private String name;
    private String email;
    private String phone;
    private boolean isActive;
}
