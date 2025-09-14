package com.admin.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminActivityLog {
    @Id @GeneratedValue
    private Long id;
    private Long adminId;
    private String actionType; // e.g., "FLAG_TRANSACTION", "UNFLAG", "ACCOUNT_STATUS_CHANGE"
    private String targetType;
    private Long targetId;
    private String details;
    private LocalDateTime timestamp;
}