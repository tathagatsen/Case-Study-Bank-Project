package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.dto.EmailDto;
import com.project.dto.PasswordUpdateNotificationCheckDto;
import com.project.dto.PasswordUpdateNotificationDto;
import com.project.dto.UserOTPNotificationDto;
import com.project.dto.UserVerificationNotificationDto;



@FeignClient(name = "02-NotificationsModule", url = "http://localhost:8085/notifications")
public interface NotificationInterface {

    @PostMapping("/register")
    boolean registerCustomer(@RequestBody UserOTPNotificationDto userOTPNotificationDto);

    @PostMapping("/verifyOTP")
    boolean verifyOtp(@RequestBody UserVerificationNotificationDto userVerificationNotificationDto);

    @PostMapping("/ChangePassword")
    void initiatePasswordChange(@RequestBody PasswordUpdateNotificationDto dto);

    @PostMapping("/sendPasswordUpdate")
    boolean sendPasswordUpdateStatus(@RequestBody PasswordUpdateNotificationCheckDto pDto);
}
