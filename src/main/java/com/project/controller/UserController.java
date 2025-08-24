package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.project.dto.EmailDto;
import com.project.service.UserService;




@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;
	
	//1.Register User
	@PostMapping("/register")
	public String registerUser(@RequestBody RegisterDto registerDto) {
		return service.registerUser(registerDto);
	}
	
	@PostMapping("/register/verification")
    public String verifyRegistration(@RequestBody OtpDto otpDto) {
        try {
            service.verifyOtp(otpDto.getEmail(), otpDto.getOtp(), false);
            return "User registered successfully!";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

	//2.Login User
    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            service.initiateLogin(loginRequestDto);
            return "OTP sent to email for login verification";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/login/verification")
    public String verifyLogin(@RequestBody OtpDto otpDto) {
        try {
            service.verifyOtp(otpDto.getEmail(), otpDto.getOtp(), true);
            return "User logged in successfully!";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    
   //3.Password Reset
    @PostMapping("/updPwd")
    public String changeOrResetPwd(@RequestBody EmailDto email) {
    	service.initiateChange(email);
    	return "Otp Sent at "+email;
    }
    
    @PutMapping("/verifyUpd")
    public void verifyUpdate(@RequestBody PassWordUpdateDto passWordUpdateDto) {
    	service.verifyUpdate(passWordUpdateDto);
    }
    
    //4.Enable 2 Factor Auth
    @PostMapping("/enable2fa")
    public String enable2Fa(@RequestBody EmailDto twoFADto) {
        //TODO: process POST request
        
        return service.enable2Fa(twoFADto);
    }
    
    
	
}
