package com.ofss.notifications.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ofss.notifications.model.Notifications;
import com.ofss.notifications.model.SupportTicket;
import com.ofss.notifications.repository.NotificationsRepository;
import com.ofss.notifications.service.EmailService;
import com.ofss.notifications.service.NotificationsService;
import com.ofss.notifications.dto.*;
import com.ofss.notifications.*;
import com.ofss.notifications.dto.NotificationTransactionDto;
import com.ofss.notifications.dto.PasswordUpdateNotificationDto;
import com.ofss.notifications.dto.UserEmailDto;
import com.ofss.notifications.dto.UserOTPNotificationDto;
import com.ofss.notifications.dto.UserVerificationNotificationDto;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {

    private final NotificationsRepository notificationsRepository;
	
	@Autowired
	NotificationsService service;
	
	@Autowired
	private EmailService emailService;

    NotificationsController(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }
	
    @GetMapping("/{id}")
	public Optional<Notifications> getById(@PathVariable Long id) {
		return service.getById(id);
	}
    @PostMapping("/send-email")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailDTO dto) {
        emailService.sendEmail(dto);
        return ResponseEntity.ok().build();
    }

	@GetMapping("/")
	public List<Notifications> getAll(){
		return service.getAll();
	}
	
	@PostMapping("/")
	public Notifications addNotification(@RequestBody Notifications notification) {
		return service.addNotification(notification);
	}
	
	@PostMapping("/register")
	public boolean registerCustomer(@RequestBody UserOTPNotificationDto userOTPNotificationDto) {
		return service.registerCustomer(userOTPNotificationDto);
	}
	
	@PostMapping("/verifyOTP")
	boolean verifyOtp(@RequestBody UserVerificationNotificationDto userVerificationNotificationDto) {
		return service.verifyOtp(userVerificationNotificationDto);
	}
	
	@PostMapping("/transactionStatus")
	public boolean transactionStatus(@RequestBody NotificationTransactionDto dto) {
		return service.transactionStatus(dto);
	}
	

    @PostMapping("/ChangePassword")
    public void initiatePasswordChange(@RequestBody PasswordUpdateNotificationDto dto) {
        service.initiatePasswordChange(dto);
    }

    @PostMapping("/sendPasswordUpdate")
    public boolean sendPasswordUpdateStatus(@RequestBody PasswordUpdateNotificationCheckDto pDto) {
        return service.sendPasswordUpdateStatus(pDto);
    }
	

    
    // ===== Support Ticket APIs =====
    @PostMapping("/notify-ticket")
    public SupportTicket createSupportTicket(@RequestBody TicketNotificationDto dto) {
        return service.saveTicketAndNotify(dto);
    }

    @GetMapping("/support-tickets")
    public java.util.List<SupportTicket> getAllTickets() {
        return service.listAllTickets();
    }

    @GetMapping("/support-tickets/customer/{customerId}")
    public java.util.List<SupportTicket> getTicketsByCustomer(@PathVariable Long customerId) {
        return service.listTicketsByCustomer(customerId);
    }

    // ===== KYC Submission Notification =====
    @PostMapping("/sent-admin-kyc")
    public void sentAdminKyc(@RequestParam Long customerId, @RequestParam String email) {
        service.sendKycSubmissionEmail(customerId, email);
    }
    
    @PutMapping("/support-ticket/resolve")
    public ResponseEntity<Void> resolveTicket(@RequestParam Long ticketId,
                                              @RequestParam Long customerId,
                                              @RequestParam String email) {
        service.resolveTicket(ticketId, customerId, email);
        return ResponseEntity.ok().build();
    }


}
