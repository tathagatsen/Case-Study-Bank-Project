package com.project.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateNotificationCheckDto {
    private String email;
    private long customerId;
    private String status; // "SUCCESS" | "FAILED"
}