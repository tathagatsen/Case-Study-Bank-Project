package com.project.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVerificationNotificationDto {
    private String email;
    private String otp;
}