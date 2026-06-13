package com.nexbank.authservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexbank.authservice.domain.User;
import com.nexbank.authservice.dto.RegisterRequest;
import com.nexbank.authservice.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@PostMapping("/register")
	public User register(@RequestBody RegisterRequest request) {
		return userService.register(request);
	}
}
