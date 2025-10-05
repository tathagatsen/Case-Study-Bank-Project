package com.project.model;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-one with UserCredential
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserCredential user;
    private Integer fails;
    private LocalDateTime lockedAt;
    private LocalDateTime unlockAt;
    private String reason;   // e.g., "Too many failed attempts"
    private boolean active;  // true = currently locked
}
