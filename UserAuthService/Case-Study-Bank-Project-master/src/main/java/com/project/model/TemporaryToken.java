//	package com.project.model;
//
//import java.time.LocalDateTime;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.ManyToOne;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class TemporaryToken {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    private UserCredential user;
//
//    private String token;
//    private LocalDateTime expiryTime;
//    
//    @Enumerated(EnumType.STRING)
//    private Purpose purpose; // PASSWORD_RESET, EMAIL_VERIFY, etc.
//    public enum Purpose{
//    	PASSWORD_RESET,
//    	EMAIL_VERIFY
//    }
//}
