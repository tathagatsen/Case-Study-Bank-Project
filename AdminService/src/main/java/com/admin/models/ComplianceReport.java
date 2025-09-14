package com.admin.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplianceReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String reportId;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin generatedBy; // report created by Admin

    private String reportType; // KYC, FRAUD, REGULATORY
    private LocalDateTime generatedAt;

    private String filePath; // Path to file storage OR DB column to store JSON data
}
