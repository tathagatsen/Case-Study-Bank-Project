package com.ofss.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateNotificationCheckDto {
    private String email;
    private long customerId;
    private String status; // "SUCCESS" | "FAILED"
}