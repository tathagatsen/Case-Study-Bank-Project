package com.nexbank.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexbank.authservice.domain.User;
import com.nexbank.authservice.dto.RegisterRequest;
import com.nexbank.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	public User register(RegisterRequest request) {
		User user=User.builder()
				.userName(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.branchId(request.getBranchId())
				.employeeCode(request.getEmployeeCode())
				.enabled(true)
				.build();
		return userRepository.save(user);
	}
}
