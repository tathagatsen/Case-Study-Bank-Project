package com.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor
public class CustomerCountDto {
    private long totalUsers;
    private long activeUsers;
}
