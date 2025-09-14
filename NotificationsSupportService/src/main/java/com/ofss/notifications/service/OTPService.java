package com.ofss.notifications.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofss.notifications.dto.EmailDTO;
import com.ofss.notifications.model.OTP;
import com.ofss.notifications.repository.OTPRepository;

import jakarta.transaction.Transactional;

@Service
public class OTPService {
	@Autowired
	OTPRepository repo;
	
	@Autowired
	EmailService emailService;
	
	public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit
        return String.valueOf(otp);
    }
	
	@Transactional
	public String sendOtp(String toEmail) {
	    String generatedOTP = generateOtp();
	    String emailText = "\nYour OTP is: " + generatedOTP;
	    EmailDTO emailDTO = new EmailDTO(toEmail, "Verify Your email", emailText);
	    OTP otp = new OTP();
	    otp.setEmail(toEmail);
	    otp.setOtp(generatedOTP);
	    otp.setGeneratedAt(LocalDateTime.now()); // <-- Set generation time
	    repo.save(otp);
	    emailService.sendEmail(emailDTO);
	    return generatedOTP;
	}
	

	@Transactional
	public boolean verifyOtp(String toEmail, String receivedOtp) {
	    Optional<OTP> otpEntry = repo.findById(toEmail);

	    if (otpEntry.isPresent()) {
	        OTP otpObj = otpEntry.get();

	        // Check if OTP has expired (1 minute = 60 seconds)
	        LocalDateTime now = LocalDateTime.now();
	        LocalDateTime generatedAt = otpObj.getGeneratedAt();
	        if (generatedAt == null || Duration.between(generatedAt, now).toMinutes() >= 1) {
	            otpObj.setOtp(null);
	            otpObj.setGeneratedAt(null);
	            repo.save(otpObj);
	            return false; // OTP expired
	        }
	        
	        boolean match = receivedOtp.equals(otpObj.getOtp());
	        otpObj.setOtp(null);// Null OTP after check (single use)
	        otpObj.setGeneratedAt(null);
	        repo.save(otpObj);
	        return match;
	    }
	    return false;
	}
	
}
