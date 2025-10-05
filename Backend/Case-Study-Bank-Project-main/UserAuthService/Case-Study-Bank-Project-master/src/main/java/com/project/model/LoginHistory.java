//package com.project.model;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class LoginHistory {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // 👇 Many LoginHistory entries belong to one User
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private UserCredential user;
//
//    private String ipAddress;
//    private String deviceInfo;
//    private LocalDateTime loginTime;
//    private boolean success; // true = success, false = failure
//}
