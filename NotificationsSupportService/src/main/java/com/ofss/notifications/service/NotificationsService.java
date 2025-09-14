package com.ofss.notifications.service;


import java.util.List;
import java.util.Optional;
import com.ofss.notifications.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ofss.notifications.controller.OTPController;
import com.ofss.notifications.dto.EmailDTO;
import com.ofss.notifications.dto.NotificationTransactionDto;
import com.ofss.notifications.dto.PasswordUpdateNotificationCheckDto;
import com.ofss.notifications.dto.PasswordUpdateNotificationDto;
import com.ofss.notifications.dto.UserEmailDto;
import com.ofss.notifications.dto.UserOTPNotificationDto;
import com.ofss.notifications.dto.UserVerificationNotificationDto;
import com.ofss.notifications.feign.CustomerTicketFeign;
import com.ofss.notifications.model.Notifications;
import com.ofss.notifications.model.Notifications.ModuleName;
import com.ofss.notifications.repository.NotificationsRepository;
import com.ofss.notifications.repository.OTPRepository;
import com.ofss.notifications.repository.SupportTicketRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificationsService {

	private final OTPController OTPController;

	private final EmailService emailService;

	@Autowired
	private NotificationsRepository repo;

	@Autowired
	private OTPRepository otpRepository;

	@Autowired
	private OTPService otpService;
	
	  @Autowired
	    private SupportTicketRepository supportTicketRepository;
	  
	  @Autowired
	  private CustomerTicketFeign customerTicketFeign;


	NotificationsService(EmailService emailService, OTPController OTPController) {
		this.emailService = emailService;
		this.OTPController = OTPController;
	}
//	
//	@Autowired
//	private EmailService emailService;

	@Transactional
	public Optional<Notifications> getById(Long id) {
		return repo.findById(id);
	}

	@Transactional
	public List<Notifications> getAll() {
		return repo.findAll();
	}

	@Transactional
	public Notifications addNotification(@RequestBody Notifications notification) {
		Notifications added = repo.save(notification);
		return added;
	}

	public boolean registerCustomer(UserOTPNotificationDto userOTPNotificationDto) {
		// TODO Auto-generated method stub
		String subject = "";
		String text = "";
		Notifications notifications = new Notifications();
		OTP otp = new OTP();

		notifications.setEmail(userOTPNotificationDto.getEmail());
		notifications.setModuleId(userOTPNotificationDto.getCustomerId());
		notifications.setStatus(com.ofss.notifications.model.Status.READ);
		notifications.setModuleName(ModuleName.USER);
		repo.save(notifications);
		otpService.sendOtp(userOTPNotificationDto.getEmail());
		return true;
	}	

//	public boolean verifyOtp(UserVerificationNotificationDto userVerificationNotificationDto) {
//		// TODO Auto-generated method stub
////		OTP otp=new OTP();
////		otp.setEmail(userVerificationNotificationDto.getEmail());
////		otp.setGeneratedAt(LocalDateTime.now());
////		otp.setOtp(userVerificationNotificationDto.getOtp());
////		otpRepository.save(otp);
//		boolean status = otpService.verifyOtp(userVerificationNotificationDto.getEmail(),
//				userVerificationNotificationDto.getOtp());
//		if (status) {
//			String subject = "";
//			String text = "";
//			EmailDTO dto = new EmailDTO();
//			dto.setSubject(subject);
//			dto.setText(text);
//			dto.setTo(userVerificationNotificationDto.getEmail());
//			emailService.sendEmail(dto);
//		} else {
//			String subject = "";
//			String text = "";
//			EmailDTO dto = new EmailDTO();
//			dto.setSubject(subject);
//			dto.setText(text);
//			dto.setTo(userVerificationNotificationDto.getEmail());
//			emailService.sendEmail(dto);
//		}
//		return status;
//	}

	public boolean transactionStatus(NotificationTransactionDto dto) {
		// TODO Auto-generated method stub
		String subjectFrom = "Sent "+dto.getAmount()+" to "+dto.getToAccountId()+" .";
		String subjectTo = "Receieved "+dto.getAmount()+" from "+dto.getFromAccountId();
		String textFrom = "Hi from "+dto.getToAccountId();
		String textTo = "Hi from "+dto.getFromAccountId();
		EmailDTO from = new EmailDTO();
		EmailDTO to = new EmailDTO();
		from.setSubject(subjectFrom);
		from.setText(textFrom);
		from.setTo(dto.getFromEmail());
		to.setSubject(subjectTo);
		to.setText(textTo);
		to.setTo(dto.getToEmail());
		//return message based on status type
		emailService.sendEmail(to);
		emailService.sendEmail(from);
		//done till here (Trx-noti,trx-acc,call email
		return true;
	}

	  public void initiatePasswordChange(PasswordUpdateNotificationDto dto) {
	        // Generate & store OTP (make sure otpService returns the code)
	        String otp = otpService.sendOtp(dto.getEmail()); // implement this to return the OTP

	        String subject = "Password change initiated for " + dto.getCustomerId();
	        String text =
	            "Dear Customer " + dto.getCustomerId() + ", a password change has been initiated by you.\n\n" +
	            "Your OTP is: " + otp + "\n" +
	            "This OTP is valid for 10 minutes.\n" +
	            "If you did not request this, please contact support.";

	        EmailDTO eDto = new EmailDTO();
	        eDto.setTo(dto.getEmail());
	        eDto.setSubject(subject);
	        eDto.setText(text);

	        emailService.sendEmail(eDto);
	    }

	    // 2) Verify OTP (used by Auth service)
	    public boolean verifyOtp(UserVerificationNotificationDto dto) {
	        return otpService.verifyOtp(dto.getEmail(), dto.getOtp());
	    }

	    // 3) Send final status email only
	    public boolean sendPasswordUpdateStatus(PasswordUpdateNotificationCheckDto pDto) {
	        String subject = "Password change " + pDto.getStatus() + " for " + pDto.getCustomerId();
	        String text = "Dear Customer " + pDto.getCustomerId() + ", your password change has " + pDto.getStatus() + ".";

	        EmailDTO eDto = new EmailDTO();
	        eDto.setTo(pDto.getEmail());
	        eDto.setSubject(subject);
	        eDto.setText(text);

	        emailService.sendEmail(eDto);
	        return true;
	    }
	    
	    
	    // ===== Support Ticket Notifications =====
	  
	    public com.ofss.notifications.model.SupportTicket saveTicketAndNotify(com.ofss.notifications.dto.TicketNotificationDto dto) {
	        com.ofss.notifications.model.SupportTicket ticket = com.ofss.notifications.model.SupportTicket.builder()
	                .customerId(dto.getCustomerId())
	                .email(dto.getEmail())
	                .subject(dto.getSubject())
	                .description(dto.getDescription())
	                .status("OPEN")
	                .createdAt(java.time.LocalDateTime.now())
	                .build();
	        supportTicketRepository.save(ticket);

	        // Send email
	        com.ofss.notifications.dto.EmailDTO eDto = new com.ofss.notifications.dto.EmailDTO();
	        eDto.setTo(dto.getEmail());
	        eDto.setSubject("Support ticket created: " + ticket.getTicketId());
	        eDto.setText("Hi, your ticket has been created.\n\nSubject: " + dto.getSubject() + "\nDescription: " + dto.getDescription() + "\nTicket ID: " + ticket.getTicketId());
	        emailService.sendEmail(eDto);

	        return ticket;
	    }

	    public List<SupportTicket> listAllTickets() {
	        return supportTicketRepository.findAll();
	    }

	    public List<SupportTicket> listTicketsByCustomer(Long customerId) {
	        return supportTicketRepository.findByCustomerId(customerId);
	    }

	    
	    // ===== KYC Notification =====
	    public void sendKycSubmissionEmail(Long customerId, String email) {
	        EmailDTO eDto = new EmailDTO();
	        eDto.setTo(email);
	        eDto.setSubject("KYC submitted for verification");
	        eDto.setText("Dear Customer " + customerId + ", your KYC document has been submitted to the admin for verification.");
	        emailService.sendEmail(eDto);
	    }
	    
	    public void resolveTicket(Long ticketId, Long customerId, String email) {
	        // 1. Update local NotificationSupportTicket status
	        SupportTicket ticket = supportTicketRepository.findById(ticketId)
	            .orElseThrow(() -> new RuntimeException("Ticket not found"));
	        ticket.setStatus("RESOLVED");
	        supportTicketRepository.save(ticket);

	        // 2. Call CustomerService to update its SupportTicket
	        customerTicketFeign.markTicketResolved(customerId, ticketId);

	        // 3. Send email to the customer
	        String subject = "Support Ticket #" + ticketId + " Resolved";
	        String body = "Hello, your support ticket with ID " + ticketId +
	                      " has been resolved. Thank you for your patience.";
	        EmailDTO dto=new EmailDTO();
	        dto.setSubject(subject);
	        dto.setText(body);
	        dto.setTo(email);
	        emailService.sendEmail(dto);
	    }
}
