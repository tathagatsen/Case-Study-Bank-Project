package com.project.dto;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoginDto {
    private Long custId;
//    private String ipAddress;
    private String deviceInfo;
}