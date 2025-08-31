package com.project.model;

import java.time.LocalDate;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;

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

	private String firstName;
	private String lastName;

	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dob;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(unique = true)
	private long userId; // 10-digit customer id

	@Column(nullable = false)
	private String password;

	private String address;
	private boolean isVerified;

	@Column(nullable = false, unique = true, length = 10)
	private String phoneNumber;

	@Column(nullable = false)
	private String role; // no @JoinColumn; plain String role

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private AccountLock accountLock;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private TwoFactorAuth twoFactorAuth;
}