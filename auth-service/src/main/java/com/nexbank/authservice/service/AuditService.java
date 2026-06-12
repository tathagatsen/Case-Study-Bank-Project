package com.nexbank.authservice.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.nexbank.authservice.audit.LoginAuditEntity;
import com.nexbank.authservice.repository.LoginAuditRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {
	private final LoginAuditRepository loginAuditRepository;
	
	public void logSuccess(String username,String ip) {
		loginAuditRepository.save(LoginAuditEntity.builder()
				.userName(username)
				.success(true)
				.ipAddress(ip)
				.loginTime(LocalDateTime.now())
				.build());
	}
	public void logFailure(String username,String ip,String reason) {
		loginAuditRepository.save(LoginAuditEntity.builder()
				.userName(username)
				.success(false)
				.ipAddress(ip)
				.loginTime(LocalDateTime.now())
				.build());
	}
}
