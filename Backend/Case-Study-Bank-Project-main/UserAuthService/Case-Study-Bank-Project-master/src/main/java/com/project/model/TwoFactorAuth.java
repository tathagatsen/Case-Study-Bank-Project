package com.project.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class TwoFactorAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserCredential user; // FK → user_credential.id

    private boolean enabled;         // true if 2FA is on
              // SMS, EMAIL, AUTH_APP
    private String secretKey;        // used for TOTP apps
    private LocalDateTime lastVerifiedAt;
    
    @Enumerated(EnumType.STRING)
    private METHOD method;
    public enum METHOD{
    	SMS,
    	EMAIL,
    	AUTHENTICATOR
    }
}
