package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.dto.UserOTPNotificationDto;
import com.project.dto.UserVerificationNotificationDto;



@FeignClient("Notification")
public interface NotificationInterface {
	
	@PostMapping("/register")
	public UserOTPNotificationDto registerCustomer(@RequestBody UserOTPNotificationDto userOTPNotificationDto);
	
	@PostMapping("/verifyOTP")
	boolean verifyOtp(@RequestBody UserVerificationNotificationDto userVerificationNotificationDto);
	
	
}
