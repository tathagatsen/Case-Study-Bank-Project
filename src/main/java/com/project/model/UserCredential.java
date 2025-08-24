package com.project.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.model.Role.RoleType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredential {
	@Id
	@UuidGenerator
	private UUID id;
	
//	@Column(nullable = false)
	private String firstName;
//	@Column(nullable = false)
	private String lastName;
	
//	@Column(nullable = false)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dob;
	
	@Column(nullable = false,unique = true)
	private String email;
	private long userId;
	
	@Column(nullable = false)
	private String password;
	
	private String address;
	private boolean isVerified;
	
//	@ColumnDefault("0")
	private String otp;
	
	@Column(nullable = false,unique = true,length = 10)
	private String phoneNumber;
	
	
    @JoinColumn(nullable = false) 
    private String role;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LoginHistory> loginHistories = new ArrayList<>();
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private AccountLock accountLock;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private TwoFactorAuth twoFactorAuth;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TemporaryToken> temporaryTokens;
	
	
}
