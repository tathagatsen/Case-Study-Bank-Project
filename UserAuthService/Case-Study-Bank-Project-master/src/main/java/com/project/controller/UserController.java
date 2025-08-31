package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.LoginRequestDto;
import com.project.dto.OtpDto;
import com.project.dto.PassWordUpdateDto;
import com.project.dto.RegisterDto;
import com.project.dto.TwoFADto;
import com.project.api.ApiResponse;
import com.project.dto.EmailDto;
import com.project.service.UserService;




@RestController
@RequestMapping("/user")
public class UserController {


@Autowired
private UserService service;


// 1. Register User
@PostMapping("/register")
public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterDto registerDto) {
ApiResponse res = service.registerUser(registerDto);
return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
}


@PostMapping("/register/verification")
public ResponseEntity<ApiResponse> verifyRegistration(@RequestBody OtpDto otpDto) {
ApiResponse res = service.verifyRegistrationOtp(otpDto);
return ResponseEntity.ok(res);
}


// 2. Login User
@PostMapping("/login")
public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
ApiResponse res = service.initiateLogin(loginRequestDto);
// either 200 OK (no 2FA) or 202 Accepted (OTP sent). We keep 200 to simplify
return ResponseEntity.ok(res);
}


@PostMapping("/login/verification")
public ResponseEntity<ApiResponse> verifyLogin(@RequestBody OtpDto otpDto) {
ApiResponse res = service.verifyLoginOtp(otpDto);
return ResponseEntity.ok(res);
}


// 3. Password Reset
@PostMapping("/updPwd")
public ResponseEntity<ApiResponse> changeOrResetPwd(@RequestBody EmailDto email) {
ApiResponse res = service.initiateChange(email);
return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
}


@PutMapping("/verifyUpd")
public ResponseEntity<ApiResponse> verifyUpdate(@RequestBody PassWordUpdateDto passWordUpdateDto) {
ApiResponse res = service.verifyUpdate(passWordUpdateDto);
return ResponseEntity.ok(res);
}


// 4. Enable 2FA
@PostMapping("/enable2fa")
public ResponseEntity<ApiResponse> enable2Fa(@RequestBody EmailDto emailDto) {
ApiResponse res = service.enable2Fa(emailDto);
return ResponseEntity.ok(res);
}
}