package com.admin.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycRequestDTO {
    private String custId;
    private List<String> requestedDocs;
    private String reason;
}
