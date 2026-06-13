package com.nexbank.authservice.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.nexbank.authservice.domain.User;
import com.nexbank.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository userRepository;
	
	public User authenticate(String username) {
		return userRepository.findByUserName(username).filter(User::isEnabled).orElseThrow(()->new BadCredentialsException("Invalid User or Disabled User"));
	}
}
