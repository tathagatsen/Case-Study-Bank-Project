package com.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.CustomerDto;
import com.project.dto.CustomerKycDto;
import com.project.dto.CustomerSupportTicketDto;
import com.project.model.Customer;
import com.project.model.KycDocument;
import com.project.model.SupportTicket;
import com.project.service.ChatBotService;
import com.project.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ChatBotService chatBotService;

	@PostMapping("/register")
	public Customer registerCustomer(@RequestBody CustomerDto customerDto) {
		return customerService.registerCustomer(customerDto);
	}
	
	@PostMapping("/login")
	public Long loginCustomer(@RequestParam Long custId) {
		return customerService.loginCustomer(custId);
	}

	@GetMapping("/details/{custId}")
	public Customer getCustomerDetails(@PathVariable Long custId) {
		return customerService.getCustomerDetails(custId);
	}
	
	@PatchMapping("{customerId}/deactivate")
	public Customer deactivateCustomer(@PathVariable Long customerId) {
		return customerService.deactivateCustomer(customerId);
	}
	
	@PostMapping("/support-ticket")
	public String createSupportTicket(@RequestBody CustomerSupportTicketDto customerSupportTicketDto) {
		return customerService.createSupportTicket(customerSupportTicketDto);
	}
	
	@GetMapping("/listAllTickets")
    public List<SupportTicket> listSupportTickets(@PathVariable Long customerId) {
		return customerService.listSupportTickets(customerId);
	}
	
	 @GetMapping("/support-ticket/{customerId}/{ticketId}")
	 public SupportTicket getSupportTicketDetails(@PathVariable Long customerId, @PathVariable Long ticketId) {
		 return customerService.getSupportTicketDetails(customerId,ticketId);
	 }
	 
	 @PostMapping("/uploadKyc/{customerId}")
	 public KycDocument uploadKycDocument(@RequestBody CustomerKycDto customerKycDto) {
		 return customerService.uploadKycDocument(customerKycDto);
	 }
	 
	 public ResponseEntity<String> askChatbotQuestion(@RequestBody Map<String,String> payload){
		 String question=payload.get("question");
		 String answer=chatBotService.getResponse(question);
		 return ResponseEntity.ok(answer);
	 }

}
