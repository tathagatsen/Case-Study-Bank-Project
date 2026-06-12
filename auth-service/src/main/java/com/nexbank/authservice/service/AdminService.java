package com.nexbank.authservice.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.nexbank.authservice.domain.Client;
import com.nexbank.authservice.domain.User;
import com.nexbank.authservice.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final ClientRepository clientRepository;
	
	public Client adminService(String clientid) {
		return clientRepository.FindByClientId(clientid).filter(Client::isActive).orElseThrow(()->new BadCredentialsException("Admin not found"));
	}
}

