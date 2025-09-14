package com.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusUpdateDto {
private String status; // "ACTIVE", "FROZEN", "SUSPENDED", "CLOSED"
}
