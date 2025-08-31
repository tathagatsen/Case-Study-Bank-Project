package com.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.api.ApiResponse;
import com.project.dto.CustomerKycDto;
import com.project.dto.CustomerSupportTicketDto;
import com.project.dto.UserCustomerDto;
import com.project.model.Customer;
import com.project.model.CustomerLoginHistory;
import com.project.model.KycDocument;
import com.project.model.SupportTicket;
import com.project.service.ChatBotService;
import com.project.service.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@RestController
//@RequestMapping("/customer")
//public class CustomerController {
//	@Autowired
//	private CustomerService customerService;
//	
//	@Autowired
//	private ChatBotService chatBotService;
//
//	@PostMapping("/register")
//	public UserCustomerDto registerCustomer(@RequestBody UserCustomerDto customerDto) {
//		return customerService.registerCustomer(customerDto);
//	}
//	
//	@PostMapping("/login")
//	public Long loginCustomer(@RequestParam Long custId) {
//		return customerService.loginCustomer(custId);
//	}
//
//	@GetMapping("/details/{custId}")
//	public Customer getCustomerDetails(@PathVariable Long custId) {
//		return customerService.getCustomerDetails(custId);
//	}
//	
//	@PatchMapping("{customerId}/deactivate")
//	public Customer deactivateCustomer(@PathVariable Long customerId) {
//		return customerService.deactivateCustomer(customerId);
//	}
//	
//	@PostMapping("/support-ticket")
//	public String createSupportTicket(@RequestBody CustomerSupportTicketDto customerSupportTicketDto) {
//		return customerService.createSupportTicket(customerSupportTicketDto);
//	}
//	
//	@GetMapping("/listAllTickets")
//    public List<SupportTicket> listSupportTickets(@PathVariable Long customerId) {
//		return customerService.listSupportTickets(customerId);
//	}
//	
//	 @GetMapping("/support-ticket/{customerId}/{ticketId}")
//	 public SupportTicket getSupportTicketDetails(@PathVariable Long customerId, @PathVariable Long ticketId) {
//		 return customerService.getSupportTicketDetails(customerId,ticketId);
//	 }
//	 
//	 @PostMapping("/uploadKyc/{customerId}")
//	 public KycDocument uploadKycDocument(@RequestBody CustomerKycDto customerKycDto) {
//		 return customerService.uploadKycDocument(customerKycDto);
//	 }
//	 
//	 @PostMapping("/ask-chatbot")
//	 public ResponseEntity<String> askChatbotQuestion(@RequestBody Map<String,String> payload){
//		 String question=payload.get("question");
//		 String answer=chatBotService.getResponse(question);
//		 return ResponseEntity.ok(answer);
//	 }
//
//}
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping("/register")
    public ResponseEntity<UserCustomerDto> registerCustomer(@RequestBody UserCustomerDto customerDto) {
        UserCustomerDto saved = customerService.registerCustomer(customerDto);
        return ResponseEntity.status(201).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<Long> loginCustomer(@RequestParam Long custId, HttpServletRequest request) {
        Long id = customerService.loginCustomer(custId, request);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/details/{custId}")
    public ResponseEntity<Customer> getCustomerDetails(@PathVariable Long custId) {
        Customer customer = customerService.getCustomerDetails(custId);
        return ResponseEntity.ok(customer);
    }

    @PatchMapping("{customerId}/deactivate")
    public ResponseEntity<ApiResponse> deactivateCustomer(@PathVariable Long customerId) {
        ApiResponse resp = customerService.deactivateCustomer(customerId);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/support-ticket")
    public ResponseEntity<ApiResponse> createSupportTicket(@RequestBody CustomerSupportTicketDto dto) {
        ApiResponse resp = customerService.createSupportTicket(dto);
        return ResponseEntity.status(201).body(resp);
    }

    @GetMapping("/listAllTickets/{customerId}")
    public ResponseEntity<List<SupportTicket>> listSupportTickets(@PathVariable Long customerId) {
        List<SupportTicket> list = customerService.listCustomerTickets(customerId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/support-ticket/{customerId}/{ticketId}")
    public ResponseEntity<SupportTicket> getSupportTicketDetails(@PathVariable Long customerId,
                                                                 @PathVariable Long ticketId) {
        SupportTicket ticket = customerService.getSupportTicketDetails(customerId, ticketId);
        return ResponseEntity.ok(ticket);
    }

    // I changed this mapping to accept DTO (it includes customerId)
    @PostMapping("/uploadKyc")
    public ResponseEntity<KycDocument> uploadKycDocument(@RequestBody CustomerKycDto customerKycDto) {
        KycDocument saved = customerService.uploadKycDocument(customerKycDto);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping("/login-history/{customerId}")
    public ResponseEntity<List<CustomerLoginHistory>> getLoginHistory(@PathVariable Long customerId) {
        // returns list of CustomerLoginHistory objects
        List<CustomerLoginHistory> history = customerService.getLoginHistory(customerId);
        return ResponseEntity.ok((List<CustomerLoginHistory>) history);
    }

    @PostMapping("/ask-chatbot")
    public ResponseEntity<String> askChatbotQuestion(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        String answer = chatBotService.getResponse(question);
        return ResponseEntity.ok(answer);
    }
}