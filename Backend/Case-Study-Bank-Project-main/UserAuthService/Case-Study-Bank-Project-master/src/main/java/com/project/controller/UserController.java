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
import com.project.dto.PasswordUpdateNotificationDto;
import com.project.dto.RegisterDto;
import com.project.dto.SetPasswordDto;
import com.project.dto.TwoFADto;
import com.project.dto.UserOTPNotificationDto;
import com.project.dto.UserResponseDto;
import com.project.model.UserCredential;
import com.project.api.ApiResponse;
import com.project.dto.AdminRequestDto;
import com.project.dto.EmailDto;
import com.project.service.UserService;

//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//	@Autowired
//	private UserService service;
//	
//	
//
//// 1. Register User
//	@PostMapping("/register")
//	public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterDto registerDto) {
//		ApiResponse res = service.registerUser(registerDto);
//		return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
//	}
//
//	@PostMapping("/register/verification")
//	public ResponseEntity<ApiResponse> verifyRegistration(@RequestBody OtpDto otpDto) {
//		ApiResponse res = service.verifyRegistrationOtp(otpDto);
//		return ResponseEntity.ok(res);
//	}
//
//// 2. Login User
//	@PostMapping("/login")
//	public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
//		ApiResponse res = service.initiateLogin(loginRequestDto);
//// either 200 OK (no 2FA) or 202 Accepted (OTP sent). We keep 200 to simplify
//		return ResponseEntity.ok(res);
//	}
//
//	@PostMapping("/login/verification")
//	public ResponseEntity<ApiResponse> verifyLogin(@RequestBody OtpDto otpDto) {
//		ApiResponse res = service.verifyLoginOtp(otpDto);
//		return ResponseEntity.ok(res);
//	}
//	
//	@PostMapping("/setNewPassword")
//	public ResponseEntity<ApiResponse> setNewPassword(@RequestBody SetPasswordDto setPasswordDto){
//		ApiResponse res=service.setNewPassword(setPasswordDto);
//		return ResponseEntity.ok(res);
//	}
//
//// 3. Password Reset
//	@PostMapping("/updPwd")
//	public ResponseEntity<ApiResponse> changeOrResetPwd(@RequestBody PasswordUpdateNotificationDto udto) {
//		ApiResponse res = service.initiateChange(udto);
//		return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
//	}
//
//	@PutMapping("/verifyUpd")
//	public ResponseEntity<ApiResponse> verifyUpdate(@RequestBody PassWordUpdateDto passWordUpdateDto) {
//		ApiResponse res = service.verifyUpdate(passWordUpdateDto);
//		return ResponseEntity.ok(res);
//	}
//
//// 4. Enable 2FA
//	@PostMapping("/enable2fa")
//	public ResponseEntity<ApiResponse> enable2Fa(@RequestBody EmailDto emailDto) {
//		ApiResponse res = service.enable2Fa(emailDto);
//		return ResponseEntity.ok(res);
//	}
//}

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;
	
	
// 1. Register User
	@PostMapping("/register")
	public ResponseEntity<Boolean> registerUser(@RequestBody RegisterDto registerDto) {
		 boolean res= service.registerUser(registerDto);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
	}
	@PostMapping("/register/verification")
	public ResponseEntity<UserResponseDto> verifyRegistration(@RequestBody OtpDto otpDto) {
		UserCredential user = service.verifyRegistrationOtp(otpDto);
		 UserResponseDto dto = new UserResponseDto(
			        user.getFirstName(),
			        user.getLastName(),
			        user.getEmail(),
			        user.getUserId(),
			        user.getPhoneNumber(),
			        user.getRole(),
			        user.isVerified()
			    );
		return ResponseEntity.ok(dto);
	}
// 2. Login User
	@PostMapping("/login")
	public ResponseEntity<Boolean> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
		boolean res = service.initiateLogin(loginRequestDto);
// either 200 OK (no 2FA) or 202 Accepted (OTP sent). We keep 200 to simplify
		return ResponseEntity.ok(res);
	}
	@PostMapping("/login/verification")
	public ResponseEntity<UserResponseDto> verifyLogin(@RequestBody OtpDto otpDto) {
		UserCredential user = service.verifyLoginOtp(otpDto);
		 UserResponseDto dto = new UserResponseDto(
			        user.getFirstName(),
			        user.getLastName(),
			        user.getEmail(),
			        user.getUserId(),
			        user.getPhoneNumber(),
			        user.getRole(),
			        user.isVerified()
			    );
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping("/setNewPassword")
	public ResponseEntity<Boolean> setNewPassword(@RequestBody SetPasswordDto setPasswordDto){
		boolean res=service.setNewPassword(setPasswordDto);
		return ResponseEntity.ok(res);
	}
// 3. Password Reset
	@PostMapping("/updPwd")
	public ResponseEntity<Boolean> changeOrResetPwd(@RequestBody PasswordUpdateNotificationDto udto) {
		boolean res = service.initiateChange(udto);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
	}
	@PutMapping("/verifyUpd")
	public ResponseEntity<UserCredential> verifyUpdate(@RequestBody PassWordUpdateDto passWordUpdateDto) {
		UserCredential res = service.verifyUpdate(passWordUpdateDto);
		return ResponseEntity.ok(res);
	}
// 4. Enable 2FA
	@PostMapping("/enable2fa")
	public ResponseEntity<ApiResponse> enable2Fa(@RequestBody EmailDto emailDto) {
		ApiResponse res = service.enable2Fa(emailDto);
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/register/admin")  //not saving in admin db from here
	public ResponseEntity<Boolean> registerAdmin(@RequestBody AdminRequestDto adminDto) {
		 boolean res= service.registerAdmin(adminDto);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
	}
	@PostMapping("/login/admin")  //send admin id
	public ResponseEntity<Boolean> loginAdmin(@RequestBody LoginRequestDto loginRequestDto) {
	    boolean res = service.initiateLoginAdmin(loginRequestDto);
	    return ResponseEntity.ok(res);
	}

}






