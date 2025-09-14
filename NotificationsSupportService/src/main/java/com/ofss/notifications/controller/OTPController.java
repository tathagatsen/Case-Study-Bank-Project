package com.ofss.notifications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ofss.notifications.service.OTPService;

@RestController
@RequestMapping("/otp")
@CrossOrigin(origins = "*")
public class OTPController {

    @Autowired
    private OTPService service;

    // Endpoint to send OTP to the provided email address
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        String otp = service.sendOtp(email);
        // In production, do not return OTP in response. Here for demo/testing only.
        return "OTP " + otp +  "sent to " + email;
    }

    // Endpoint to verify email and OTP
    @PostMapping("/verify-otp")
    public boolean verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean valid = service.verifyOtp(email, otp);
        return valid;
    }
}