package com.project.dto;
import java.time.LocalDateTime;

import org.springframework.data.repository.NoRepositoryBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class KycDocumentDto {
    private Long id;
    private String docType;
    private String docNumber;
    private String fileUrl;
    private String status;
    private LocalDateTime uploadedAt;
}
