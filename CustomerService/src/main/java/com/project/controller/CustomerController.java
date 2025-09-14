package com.project.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.api.ApiResponse;
import com.project.dto.AccountRequestDto;
import com.project.dto.AccountResponseDto;
import com.project.dto.CustomerAdminDto;
import com.project.dto.CustomerAdminKycDto;
import com.project.dto.CustomerKycDto;
import com.project.dto.CustomerKycStatusUpdateDto;
import com.project.dto.CustomerLoginDto;
import com.project.dto.CustomerSupportTicketDto;
import com.project.dto.KycDocumentDto;
import com.project.dto.KycDocumentDto2;
import com.project.dto.SupportTicketResponseDto;
import com.project.dto.UserCustomerDto;
import com.project.model.Customer;
import com.project.model.CustomerLoginHistory;
import com.project.model.KycDocument;
import com.project.model.SupportTicket;
import com.project.repository.CustomerLoginHistoryRepository;
import com.project.repository.CustomerSupportTicketRepository;
import com.project.service.ChatBotService;
import com.project.service.CustomerKycService;
import com.project.service.CustomerService;
import com.project.service.KycDocumentMapper;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


//@RestController
//@RequestMapping("/customer")
//public class CustomerController {
//
//    @Autowired
//    private CustomerService customerService;
//
//    @Autowired
//    private ChatBotService chatBotService;
//    
//    @PostMapping("/register")
//    public ResponseEntity<UserCustomerDto> registerCustomer(@RequestBody UserCustomerDto customerDto) {
//        UserCustomerDto saved = customerService.registerCustomer(customerDto);
//        return ResponseEntity.status(201).body(saved);
//    }
//    	
//    @PostMapping("/login")
//    public ResponseEntity<Long> loginCustomer(@RequestBody CustomerLoginDto loginDto) {
//        Long id = customerService.loginCustomer(loginDto);
//        return ResponseEntity.ok(id);
//    }
//    
//    @GetMapping("/details/{custId}")
//    public ResponseEntity<Customer> getCustomerDetails(@PathVariable Long custId) {
//        Customer customer = customerService.getCustomerDetails(custId);
//        return ResponseEntity.ok(customer);
//    }
//    
//    @PatchMapping("{customerId}/deactivate")
//    public ResponseEntity<ApiResponse> deactivateCustomer(@PathVariable Long customerId) {
//        ApiResponse resp = customerService.deactivateCustomer(customerId);
//        return ResponseEntity.ok(resp);
//    }
//    	
//  //Support tickets
//    @PostMapping("/support-ticket")
//    public ResponseEntity<ApiResponse> createSupportTicket(@RequestBody CustomerSupportTicketDto dto) {
//        ApiResponse resp = customerService.createSupportTicket(dto);
//        return ResponseEntity.status(201).body(resp);
//    }
//    
//    @GetMapping("/listAllTickets/{customerId}")
//    public ResponseEntity<List<SupportTicketResponseDto>> listSupportTickets(@PathVariable Long customerId) {
//        List<SupportTicket> list = customerService.listCustomerTickets(customerId);
//        List<SupportTicketResponseDto> listAll= new ArrayList<>();
//        for(SupportTicket ticket:list) {
//        	SupportTicketResponseDto dto= new SupportTicketResponseDto(
//            		ticket.getTicketId(),
//            		ticket.getSubject(),
//            		ticket.getDescription(),
//            		ticket.getStatus(),
//            		ticket.getCreatedAt()
//            		);
//        	listAll.add(dto);
//        	
//        }
//        return ResponseEntity.ok(listAll);
//    }  
//
//    @GetMapping("/support-ticket/{customerId}/{ticketId}")
//    public ResponseEntity<SupportTicketResponseDto> getSupportTicketDetails(@PathVariable Long customerId,
//                                                                 @PathVariable Long ticketId) {
//        SupportTicket ticket = customerService.getSupportTicketDetails(customerId, ticketId);
//        SupportTicketResponseDto dto= new SupportTicketResponseDto(
//        		ticket.getTicketId(),
//        		ticket.getSubject(),
//        		ticket.getDescription(),
//        		ticket.getStatus(),
//        		ticket.getCreatedAt()
//        		);
//        return ResponseEntity.ok(dto);
//    }
//    
//    //KYC
//    // I changed this mapping to accept DTO (it includes customerId)
//    @PostMapping("/uploadKyc")
//    public ResponseEntity<KycDocument> uploadKycDocument(@RequestBody CustomerKycDto customerKycDto) {
//        KycDocument saved = customerService.uploadKycDocument(customerKycDto);
//        return ResponseEntity.status(201).body(saved);
//    }
//    
//    @GetMapping("/login-history/{customerId}")
//    public ResponseEntity<List<CustomerLoginHistory>> getLoginHistory(@PathVariable Long customerId) {
//        // returns list of CustomerLoginHistory objects
//        List<CustomerLoginHistory> history = customerService.getLoginHistory(customerId);
//        return ResponseEntity.ok((List<CustomerLoginHistory>) history);
//    }
//    
//    //Chatbot
//    @PostMapping("/ask-chatbot")
//    public ResponseEntity<String> askChatbotQuestion(@RequestBody Map<String, String> payload) {
//        String question = payload.get("question");
//        String answer = chatBotService.getResponse(question);
//        return ResponseEntity.ok(answer);
//    }
//    
//    //Account
//    @PostMapping("/createAccount")
//    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto dto) {
//        AccountResponseDto response = customerService.createAccount(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//    
//    @PutMapping("/support-ticket/resolve")
//    public ResponseEntity<Void> markTicketResolved(@RequestParam Long customerId,
//                                                   @RequestParam Long ticketId) {
//        customerService.markTicketResolved(customerId, ticketId);
//        return ResponseEntity.ok().build();
//    }
//    
// // Admin callback to update KYC status
//    @PostMapping("/admin/kyc/status")
//    public ResponseEntity<Void> updateKycStatusFromAdmin(@RequestBody CustomerKycStatusUpdateDto dto) {
//        customerService.processKycStatusUpdate(dto);
//        return ResponseEntity.ok().build();
//    }
//    
//    @GetMapping("/admin/kycs/pending")
//    public ResponseEntity<List<CustomerAdminKycDto>> getPendingKycForAdmin() {
//        return ResponseEntity.ok(customerService.getPendingKycForAdmin());
//    }
//    
////    @PostMapping("/customers/{customerId}/kyc")
////    public ResponseEntity<KycDocumentDto> addKyc(@PathVariable Long customerId, @RequestBody KycDocumentDto dto) {
////        KycDocumentDto saved = kycService.save(customerId, dto);
////        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
////    }
////
////    @GetMapping("/customers/{customerId}/kyc")
////    public ResponseEntity<List<KycDocumentDto>> list(@PathVariable Long customerId) {
////        return ResponseEntity.ok(kycService.findByCustomer(customerId));
////    }
//
//}

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
    private CustomerSupportTicketRepository customerSupportTicketRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ChatBotService chatBotService;
    
    @Autowired
    private CustomerLoginHistoryRepository customerLoginHistoryRepository;

    CustomerController(CustomerSupportTicketRepository customerSupportTicketRepository) {
        this.customerSupportTicketRepository = customerSupportTicketRepository;
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserCustomerDto> registerCustomer(@RequestBody UserCustomerDto customerDto) {
        UserCustomerDto saved = customerService.registerCustomer(customerDto);
        return ResponseEntity.status(201).body(saved);
    }
    	
    @PostMapping("/login")
    public ResponseEntity<Long> loginCustomer(@RequestBody CustomerLoginDto loginDto) {
        Long id = customerService.loginCustomer(loginDto);
        return ResponseEntity.ok(id);
    }
    
    @GetMapping("/details/{custId}")
    public ResponseEntity<CustomerAdminDto> getCustomerDetails(@PathVariable Long custId) {
        Customer customer = customerService.getCustomerDetails(custId);
        
        CustomerAdminDto customerDto = new CustomerAdminDto(
        			customer.getCustomerId(),
        			customer.getName(),
        			customer.getEmail(),
        			customer.getPhone(),
        			customer.isActive()
        		);
        return ResponseEntity.ok(customerDto);
    }
    
//    @PatchMapping("{customerId}/deactivate")
//    public ResponseEntity<ApiResponse> deactivateCustomer(@PathVariable Long customerId) {
//        ApiResponse resp = customerService.deactivateCustomer(customerId);
//        return ResponseEntity.ok(resp);
//    }
    	
  //Support tickets
    @PostMapping("/support-ticket")
    public ResponseEntity<ApiResponse> createSupportTicket(@RequestBody CustomerSupportTicketDto dto) {
        ApiResponse resp = customerService.createSupportTicket(dto);
        return ResponseEntity.status(201).body(resp);
    }
    
    public List<SupportTicket> listAllTickets() {
	    return customerSupportTicketRepository.findAll();
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
    
    //KYC
    // I changed this mapping to accept DTO (it includes customerId)
//    @PostMapping("/uploadKyc")
//    public ResponseEntity<KycDocument> uploadKycDocument(@RequestBody CustomerKycDto customerKycDto) {
//        KycDocument saved = customerService.uploadKycDocument(customerKycDto);
//        return ResponseEntity.status(201).body(saved);
//    }
//    
    
    @GetMapping("/login-history")
    public ResponseEntity<List<CustomerLoginHistory>> getLoginHistoryQuery(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        try {
            java.time.LocalDateTime fromDt = null;
            java.time.LocalDateTime toDt = null;
            java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ISO_DATE_TIME;
            java.time.format.DateTimeFormatter shortDate = java.time.format.DateTimeFormatter.ISO_DATE;

            if (from != null && !from.isBlank()) {
                try {
                    fromDt = java.time.LocalDateTime.parse(from, dateFormatter);
                } catch (Exception ex) {
                    fromDt = java.time.LocalDate.parse(from, shortDate).atStartOfDay();
                }
            }
            if (to != null && !to.isBlank()) {
                try {
                    toDt = java.time.LocalDateTime.parse(to, dateFormatter);
                } catch (Exception ex) {
                    toDt = java.time.LocalDate.parse(to, shortDate).atTime(23,59,59);
                }
            }

            if (customerId != null && fromDt != null && toDt != null) {
                return ResponseEntity.ok(customerService.getLoginHistory(customerId, fromDt, toDt));
            } else if (customerId != null) {
                return ResponseEntity.ok(customerService.getLoginHistory(customerId));
            } else if (fromDt != null && toDt != null) {
                return ResponseEntity.ok(customerService.getLoginHistoryBetween(fromDt, toDt));
            } else {
                return ResponseEntity.ok(customerLoginHistoryRepository.findAll());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/login-history/{customerId}")
    public ResponseEntity<List<CustomerLoginHistory>> getLoginHistory(@PathVariable Long customerId) {
        // returns list of CustomerLoginHistory objects
        List<CustomerLoginHistory> history = customerService.getLoginHistory(customerId);
        return ResponseEntity.ok((List<CustomerLoginHistory>) history);
    }
    
    //Chatbot
    @PostMapping("/ask-chatbot")
    public ResponseEntity<String> askChatbotQuestion(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        String answer = chatBotService.getResponse(question);
        return ResponseEntity.ok(answer);
    }
    
    //Account
    @PostMapping("/createAccount")
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto dto) {
        AccountResponseDto response = customerService.createAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/support-ticket/resolve")
    public ResponseEntity<Void> markTicketResolved(@RequestParam Long customerId,
                                                   @RequestParam Long ticketId) {
        customerService.markTicketResolved(customerId, ticketId);
        return ResponseEntity.ok().build();
    }
    
// // Admin callback to update KYC status
//    @PostMapping("/admin/kyc/status")
//    public ResponseEntity<Void> updateKycStatusFromAdmin(@RequestBody CustomerKycStatusUpdateDto dto) {
//        customerService.processKycStatusUpdate(dto);
//        return ResponseEntity.ok().build();
//    }
//    
//    @GetMapping("/admin/kycs/pending")
//    public ResponseEntity<List<CustomerAdminKycDto>> getPendingKycForAdmin() {
//        return ResponseEntity.ok(customerService.getPendingKycForAdmin());
//    }
//    
    @Autowired
    private CustomerKycService service;

    @PostMapping(path="kyc/upload", consumes="multipart/form-data")
    public ResponseEntity<KycDocumentDto> uploadKyc(
            @RequestParam("customerId") Long customerId,
            @RequestParam("documentType") String documentType,
            @RequestParam("filePath") String filePath,
            @RequestParam("data") MultipartFile data
    ) {
        try {
            // Convert MultipartFile to byte[]
            byte[] fileBytes = data.getBytes();

            CustomerKycDto customerKycDto = new CustomerKycDto(customerId, documentType, filePath, fileBytes);
            System.out.println(customerKycDto);

            KycDocument kycDocument = service.uploadKyc(customerKycDto); // assumes service expects the DTO
            
            KycDocumentDto kycDocumentDto = new KycDocumentDto(
                kycDocument.getKycId(),
                kycDocument.getDocumentType(),
                kycDocument.getRemarks(),
                kycDocument.getFilePath(),
                kycDocument.getStatus(),
                kycDocument.getUploadedAt()
            );
            return ResponseEntity.ok(kycDocumentDto);

        } catch (Exception e) {
            System.out.println(e + " ERROR");
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/kyc/getAll/{customerId}")
    public ResponseEntity<List<KycDocumentDto>> getAllKycForCustomer(@PathVariable("customerId") Long customerId) {
        try {
            // Assume your service returns a list of DTOs for the given customerId
            List<KycDocumentDto> documents = service.getAllKycForCustomer(customerId);

            if (documents == null || documents.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(documents);

        } catch (Exception e) {
            System.out.println("Error while fetching KYC docs: " + e);
            return ResponseEntity.status(500).build();
        }
    }
    @PutMapping("/kyc/status")
    public ResponseEntity<Void> updateKycStatus(@RequestBody CustomerKycStatusUpdateDto dto) {
        service.updateKycStatus(dto);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/kyc/getAll")
    public ResponseEntity<List<KycDocumentDto2>> getAllKyc() {
    List<KycDocument> list = service.getAllKyc(); // ensure this runs in a read-only transaction or fetches needed fields
    List<KycDocumentDto2> dtos = list.stream()
    .map(KycDocumentMapper::toDto)
    .collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
    }
    
    
    @GetMapping("/listAllTickets")
    public ResponseEntity<List<SupportTicket>> listAllSupportTickets() {
        List<SupportTicket> list = customerService.listAllTickets(); // New service method for all tickets
        return ResponseEntity.ok(list);
    }
    
   

    @PutMapping("/deactivate/{customerId}")
    public boolean deactivateCustomer(@PathVariable Long customerId) {
        Boolean resBoolean= customerService.deactivateCustomer(customerId);
//        return ResponseEntity.ok("Customer " + customerId + " deactivated");
        return resBoolean;
    }
    
    @GetMapping("/listAll")
    public ResponseEntity<List<CustomerAdminDto>> listAllCustomers(){
    	List<Customer> customers = service.listAllCustomers();

        List<CustomerAdminDto> dtoList = customers.stream().map(customer -> new CustomerAdminDto(
            customer.getCustomerId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.isActive()
        )).collect(Collectors.toList());
        
        
        return ResponseEntity.ok(dtoList);
        
    				
    }

}