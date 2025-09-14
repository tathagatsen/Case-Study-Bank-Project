package com.ofss.notifications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.JavaMailSender;

import com.ofss.notifications.dto.AccountDTO;
import com.ofss.notifications.dto.EmailDTO;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(EmailDTO emailDTO) {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(emailDTO.getTo());
	    message.setSubject(emailDTO.getSubject());
	    message.setText(emailDTO.getText());
	    
	    
	    mailSender.send(message);
	}
}
