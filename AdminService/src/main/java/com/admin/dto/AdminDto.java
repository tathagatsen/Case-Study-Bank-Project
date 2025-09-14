package com.admin.dto;

public class AdminDto {

    // Account related
    public record AccountStatusDto(String status) {}

    // Customer related
    public record CustomerDto(String id, String name, String email) {}

    // KYC related
    public record KycRequestDto(String documentType, String documentId) {}
    
    // Notifications
    public record NotificationDto(String message, String type) {}

    // Transactions
    public record TransactionAlertDto(String transactionId, String alertType) {}
}
