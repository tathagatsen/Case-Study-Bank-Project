package com.project.model;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

//@Entity
//@Getter @Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class KycDocument {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String documentType; // Aadhaar, PAN, Passport
//    private String filePath;     // local storage path of the PDF
//    private String status;       // PENDING, APPROVED, REJECTED
//
//    private String reviewedBy;   // admin id/name (nullable)
//    private LocalDateTime reviewedAt; // nullable
//    private String remarks;      // optional admin remarks
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_id", nullable = false)
//    private Customer customer;
//
//    private LocalDateTime uploadedAt;
//}

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KycDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long kycId;
    
    @Column(name = "customer_id", insertable = false, updatable = false)
    @JsonBackReference
    private Long customerId;
    
    
    private String documentType;
    private String filePath;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime uploadedAt;
    private LocalDateTime reviewedAt;
    private String reviewedBy;
    private String remarks;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}