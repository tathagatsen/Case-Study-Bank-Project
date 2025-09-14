package com.ofss.notifications.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OTP {
	
	@Id
	public String email;
	
	@Column
	public String otp;
	
	@Column
    private LocalDateTime generatedAt;

	public OTP(String email, String otp, LocalDateTime generatedAt) {
		super();
		this.email = email;
		this.otp = otp;
		this.generatedAt = generatedAt;
	}

	public OTP() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(LocalDateTime generatedAt) {
		this.generatedAt = generatedAt;
	}

	@Override
	public String toString() {
		return "OTP [email=" + email + ", otp=" + otp + ", generatedAt=" + generatedAt + "]";
	}
	
	

	
	
	
}
