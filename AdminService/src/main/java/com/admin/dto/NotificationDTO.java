package com.admin.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String recipientId;
    private String message;
    private String type; // EMAIL, SMS, PUSH
}
