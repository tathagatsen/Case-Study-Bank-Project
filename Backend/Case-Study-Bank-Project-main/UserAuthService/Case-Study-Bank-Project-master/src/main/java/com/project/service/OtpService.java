package com.project.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

//    private final Random random = new Random();
//    private final EmailService emailService;
//
//    @Autowired
//    public OtpService(EmailService emailService) {
//        this.emailService = emailService;
//    }
//
//    public String generateOtp() {
//        int otpValue = 100000 + random.nextInt(900000);
//        return String.valueOf(otpValue);
//    }
//
//    public void sendOtpEmail(String email, String otp, String purpose) {
//        String subject = purpose + " OTP Verification";
//        String body = "Your " + purpose + " OTP is: " + otp;
//        emailService.sendEmail(email, subject, body);
//    }
}
