package com.project.model;


import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private Long customerId; // comes from Auth/User service

    private String name;
    private String email;
    private String phone;
    private String address;
    private boolean isActive;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    
    

    private String profilePhotoPath; // local/cloud storage path

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<KycDocument> kycDocuments;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SupportTicket> supportTickets;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CustomerLoginHistory> loginHistories;
}
