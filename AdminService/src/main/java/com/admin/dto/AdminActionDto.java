package com.admin.dto;
import lombok.*;
//AdminActionDto - what UI sends for review
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminActionDto {
 private Long adminId;
 private String remarks;
}
