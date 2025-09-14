//package com.project.model;
//
//import jakarta.persistence.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "accounts")
//public class Account {
//
//    @Id
//    @Column(name = "id", length = 36)
//    private String id;  // UUID-style primary key
//
//    @Column(name = "account_number", nullable = false, unique = true, length = 20)
//    private String accountNumber;
//
//    @Column(name = "account_holder_name", nullable = false, length = 100)
//    private String accountHolderName;
//
//    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
//    private BigDecimal balance;
//
//    @Column(name = "status", length = 20)
//    private String status;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    // Getters & Setters
//    public String getId() {
//        return id;
//    }
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getAccountNumber() {
//        return accountNumber;
//    }
//    public void setAccountNumber(String accountNumber) {
//        this.accountNumber = accountNumber;
//    }
//
//    public String getAccountHolderName() {
//        return accountHolderName;
//    }
//    public void setAccountHolderName(String accountHolderName) {
//        this.accountHolderName = accountHolderName;
//    }
//
//    public BigDecimal getBalance() {
//        return balance;
//    }
//    public void setBalance(BigDecimal balance) {
//        this.balance = balance;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//}
