//package com.project.service;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.project.api.ApiResponse;
//import com.project.dto.CustomerAdminKycDto;
//import com.project.dto.CustomerKycDto;
//import com.project.dto.CustomerSupportTicketDto;
//import com.project.dto.TicketNotificationDto;
//import com.project.dto.UserCustomerDto;
//import com.project.exception.ResourceNotFound;
//import com.project.feign.CustomerAdminInterface;
//import com.project.feign.CustomerTicketNotificationInterface;
//import com.project.model.Customer;
//import com.project.model.CustomerLoginHistory;
//import com.project.model.KycDocument;
//import com.project.model.SupportTicket;
//import com.project.repository.CustomerKycRepository;
//import com.project.repository.CustomerLoginHistoryRepository;
//import com.project.repository.CustomerRepository;
//import com.project.repository.CustomerSupportTicketRepository;
//
//@Service
//public class CustomerService {
//	@Autowired
//	private CustomerRepository customerRepository;
//	
//	@Autowired
//	private CustomerLoginHistoryRepository customerLoginHistoryRepository;
//	
//	@Autowired
//	private CustomerSupportTicketRepository customerSupportTicketRepository;
//	
//	@Autowired
//	private CustomerKycRepository customerKycRepository;
//	
//	@Autowired
//	private CustomerTicketNotificationInterface customerTicketNotificationInterface;
//	
//	@Autowired
//	private CustomerAdminInterface customerAdminInterface;
//	//1
//	public UserCustomerDto registerCustomer(UserCustomerDto customerDto) {
//		if (customerRepository.findById(customerDto.getCustomerId()) == null) {
//			Customer c = new Customer();
//			//send to admin service remaining
//			
//			c.setActive(true);
//			c.setCustomerId(customerDto.getCustomerId());
//			c.setName(customerDto.getName());
//			c.setEmail(customerDto.getName());
//			c.setPhone(customerDto.getPhone());
//			c.setAddress(customerDto.getAddress());
//			c.setDob(customerDto.getDob());
//			customerRepository.save(c);
//		} else {
//			throw new RuntimeException("Already Registered!!");
//		}
//		return null;
//	}
////	
//////	2
////	public Customer getCustomerDetails(Long custId) {
////		Optional<Customer> c = repository.findById(custId);
////		return c.get();
////	}
//////3
////	public Customer deactivateCustomer(Long customerId) {
////		//get AdminCustomerDto response from admin service and set isActive to false
////		//Also send notification to customer id regarding setting Customer inactive.
////		return null;
////	}
//////4
////	public Long loginCustomer(Long custId) {
////		Optional<Customer> c=repository.findById(custId);
////		CustomerLoginHistory custLoginHistory=new CustomerLoginHistory();
////		custLoginHistory.setCustomer(c.get());
////		custLoginHistory.setLoginTime(LocalDateTime.now());
//////		custLoginHistory.setIpAddress(getClientIpAddress(null));
////		customerLoginHistoryRepository.save(custLoginHistory);
////		List<CustomerLoginHistory> list=customerLoginHistoryRepository.findAllByCustomer(c.get());
////		c.get().setLoginHistories(list);
////		repository.save(c.get());
////		return null;
////	}
//////	public String getClientIpAddress(HttpServletRequest request) {
//////        String ipAddress = request.getRemoteAddr();
//////        return ipAddress;
////		//this to be implemented in user while passing through feign
//////	String ipAddress = getClientIpAddress(request);
//////    return customerClient.loginCustomer(custId, ipAddress);
//////    }
////	
//////	5
////	public String createSupportTicket(CustomerSupportTicketDto customerSupportTicketDto) {
////		// TODO Auto-generated method stub
////		Optional<Customer> c=repository.findById(customerSupportTicketDto.getCustomerId());
////		SupportTicket supportTicket=new SupportTicket();
////		supportTicket.setCreatedAt(LocalDateTime.now());
////		supportTicket.setCustomer(c.get());
////		supportTicket.setDescription(customerSupportTicketDto.getDescription());
////		supportTicket.setStatus("OPEN");
////		supportTicket.setSubject(customerSupportTicketDto.getSubject());
////		customerSupportTicketRepository.save(supportTicket);
////		
////		List<SupportTicket> list=customerSupportTicketRepository.findAllByCustomer(c.get());
////		c.get().setSupportTickets(list);
////		repository.save(c.get());
////		TicketNotificationDto tdto=new TicketNotificationDto();
////		tdto.setCustomerId(customerSupportTicketDto.getCustomerId());
////		tdto.setDescription(customerSupportTicketDto.getDescription());
////		tdto.setSubject(customerSupportTicketDto.getSubject());
////		customerTicketNotificationInterface.RaiseTicketNotification(tdto);
////		return "Support ticket Created";
////	}
////
////	public List<SupportTicket> listSupportTickets(Long customerId) {
////		// TODO Auto-generated method stub
////		Optional<Customer> c=repository.findById(customerId);
////		return customerSupportTicketRepository.findAllByCustomer(c.get());
////	}
////
////	public SupportTicket getSupportTicketDetails(Long customerId, Long ticketId) {
////		// TODO Auto-generated method stub
////		return customerSupportTicketRepository.findByIdAndCustomer(customerId,ticketId);
////	}
////	
//////6
////	public KycDocument uploadKycDocument(CustomerKycDto customerKycDto) {
////		// TODO Auto-generated method stub
////		Optional<Customer> c=repository.findById(customerKycDto.getCustomerId());
////		//send to admin service file path and other info
////		KycDocument kycDocument=new KycDocument();
////		CustomerAdminKycDto cdto=new CustomerAdminKycDto();
////		
////		kycDocument.setDocumentType(customerKycDto.getDocumentType());
////		kycDocument.setCustomer(c.get());
////		kycDocument.setFilePath(customerKycDto.getFilePath());
////		customerKycRepository.save(kycDocument);
////		List<KycDocument> list=customerKycRepository.findAllByCustomer(c.get());
////		c.get().setKycDocuments(list);
////		cdto.setCustomerId(c.get().getCustomerId());
////		cdto.setDocumentType(customerKycDto.getDocumentType());
////		cdto.setFilePath(customerKycDto.getFilePath());
////		repository.save(c.get());
////		customerAdminInterface.sendKycDetails(cdto);
////		customerTicketNotificationInterface.SendAdminKycDetails(c.get().getCustomerId());
////		return null;
////	}
//	// -------------------- Register / Upsert --------------------
//	public UserCustomerDto registerCustomer(UserCustomerDto dto) {
//	if (dto == null || dto.getCustomerId() == null) {
//	throw new IllegalArgumentException("customerId is required");
//	}
//	Customer customer = customerRepository.findById(dto.getCustomerId()).orElse(new Customer());
//	customer.setCustomerId(dto.getCustomerId());
//	customer.setName(dto.getName());
//	customer.setEmail(dto.getEmail());
//	customer.setPhone(dto.getPhone());
//	customer.setDob(dto.getDob());
//	customer.setAddress(dto.getAddress());
//	if (customer.getCreatedAt() == null) customer.setCreatedAt(LocalDateTime.now());
//	customer.setActive(true);
//	customerRepository.save(customer);
//	return dto;
//	}
//
//
//	// -------------------- Login + History --------------------
//	public Long loginCustomer(Long custId, HttpServletRequest request) {
//	Customer customer = customerRepository.findById(custId)
//	.orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + custId));
//	if (!Boolean.TRUE.equals(customer.getActive())) {
//	throw new IllegalStateException("Customer account is deactivated");
//	}
//	String ip = getClientIpAddress(request);
//	saveLoginHistoryInternal(customer, ip);
//	return customer.getCustomerId();
//	}
//
//
//	private void saveLoginHistoryInternal(Customer customer, String ipAddress) {
//	CustomerLoginHistory history = new CustomerLoginHistory();
//	history.setCustomer(customer);
//	history.setLoginTime(LocalDateTime.now());
//	history.setIpAddress(ipAddress);
//	customerLoginHistoryRepository.save(history);
//	}
//
//
//	public ResponseEntity<ApiResponse> saveLoginHistory(Long customerId, String ipAddress) {
//	Customer customer = customerRepository.findById(customerId)
//	.orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + customerId));
//	saveLoginHistoryInternal(customer, ipAddress);
//	return ResponseEntity.ok(new ApiResponse(true, "Login history saved successfully"));
//	}
//
package com.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.*;

import com.project.model.*;

import com.project.exception.ResourceNotFound;
import com.project.api.ApiResponse;
import com.project.feign.CustomerAdminInterface;
import com.project.feign.CustomerTicketNotificationInterface;
import com.project.repository.CustomerKycRepository;
import com.project.repository.CustomerLoginHistoryRepository;
import com.project.repository.CustomerRepository;
import com.project.repository.CustomerSupportTicketRepository;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerLoginHistoryRepository customerLoginHistoryRepository;

    @Autowired
    private CustomerSupportTicketRepository customerSupportTicketRepository;

    @Autowired
    private CustomerKycRepository customerKycRepository;

    @Autowired
    private CustomerTicketNotificationInterface customerTicketNotificationInterface;

    @Autowired
    private CustomerAdminInterface customerAdminInterface;

    // 1. Register Customer
    public UserCustomerDto registerCustomer(UserCustomerDto customerDto) {
        if (customerDto == null) throw new IllegalArgumentException("Customer data must be provided");
        Long id = customerDto.getCustomerId();
        if (id == null) throw new IllegalArgumentException("customerId must be provided");

        if (customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Already Registered!!");
        }

        Customer c = new Customer();
        c.setCustomerId(customerDto.getCustomerId());
        c.setName(customerDto.getName());
        c.setEmail(customerDto.getEmail());
        c.setPhone(customerDto.getPhone());
        c.setAddress(customerDto.getAddress());
        c.setDob(customerDto.getDob());
        c.setActive(true);

        customerRepository.save(c);
        // return the dto as confirmation (you can map more fields if required)
        return customerDto;
    }

    // 2. Login customer (save login history). Returns customerId on success.
    public Long loginCustomer(Long custId, HttpServletRequest request) {
        Customer c = customerRepository.findById(custId)
                .orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + custId));

        CustomerLoginHistory hist = new CustomerLoginHistory();
        hist.setCustomer(c);
        hist.setLoginTime(LocalDateTime.now());

        // capture IP from request if available
        if (request != null) {
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isBlank()) ip = request.getRemoteAddr();
            hist.setIpAddress(ip);
        }

        customerLoginHistoryRepository.save(hist);

        // update customer's loginHistory list (optional)
        List<CustomerLoginHistory> list = customerLoginHistoryRepository.findAllByCustomer(c);
        c.setLoginHistories(list);
        customerRepository.save(c);

        return c.getCustomerId();
    }

    // 3. Get customer details
    public Customer getCustomerDetails(Long custId) {
        return customerRepository.findById(custId)
                .orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + custId));
    }

    // 4. Deactivate customer
    public ApiResponse deactivateCustomer(Long customerId) {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + customerId));

        c.setActive(false);
        customerRepository.save(c);

        // TODO: if admin needs to be informed, call customerAdminInterface here.
        return new ApiResponse(true, "Customer deactivated successfully");
    }

    // 5. Create support ticket
    public ApiResponse createSupportTicket(CustomerSupportTicketDto dto) {
        Customer c = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + dto.getCustomerId()));

        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setCreatedAt(LocalDateTime.now());
        supportTicket.setCustomer(c);
        supportTicket.setDescription(dto.getDescription());
        supportTicket.setStatus("OPEN");
        supportTicket.setSubject(dto.getSubject());
        customerSupportTicketRepository.save(supportTicket);

        // update customer's tickets list
        List<SupportTicket> list = customerSupportTicketRepository.findAllByCustomer(c);
        c.setSupportTickets(list);
        customerRepository.save(c);

        // notify Notification service (fire-and-forget)
        TicketNotificationDto tdto = new TicketNotificationDto();
        tdto.setCustomerId(dto.getCustomerId());
        tdto.setDescription(dto.getDescription());
        tdto.setSubject(dto.getSubject());
        try {
            customerTicketNotificationInterface.sendTicketNotification(tdto);
        } catch (Exception ex) {
            // log if you have logger; failure of notification shouldn't break ticket creation
        }

        return new ApiResponse(true, "Support ticket Created");
    }

    // 6. List support tickets for a customer
    public List<SupportTicket> listCustomerTickets(Long customerId) {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + customerId));
        return customerSupportTicketRepository.findAllByCustomer(c);
    }

    // 7. Get details for a specific support ticket (ensure it belongs to the given customer)
    public SupportTicket getSupportTicketDetails(Long customerId, Long ticketId) {
        // repository method returns ticket for (customerId,ticketId) or null
        SupportTicket st = customerSupportTicketRepository.findByIdAndCustomer(customerId, ticketId);
        if (st == null) {
            throw new ResourceNotFound("Support ticket not found with id: " + ticketId + " for customer: " + customerId);
        }
        return st;
    }

    // 8. Upload KYC document and notify admin
    public KycDocument uploadKycDocument(CustomerKycDto customerKycDto) {
        Customer c = customerRepository.findById(customerKycDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + customerKycDto.getCustomerId()));

        KycDocument kycDocument = new KycDocument();
        kycDocument.setDocumentType(customerKycDto.getDocumentType());
        kycDocument.setCustomer(c);
        kycDocument.setFilePath(customerKycDto.getFilePath());
        // if your KycDocument entity has `status`, set default:
        try {
            // try setStatus if field exists (if using enum field this line should match)
            kycDocument.getClass().getDeclaredField("status");
            // reflection only to avoid compile error in projects without `status` field
            // prefer explicit: kycDocument.setStatus("PENDING");
        } catch (NoSuchFieldException nsf) {
            // ignore if no status field
        }

        customerKycRepository.save(kycDocument);

        // update customer's documents list
        List<KycDocument> list = customerKycRepository.findAllByCustomer(c);
        c.setKycDocuments(list);
        customerRepository.save(c);

        // notify admin service
        CustomerAdminKycDto cdto = new CustomerAdminKycDto();
        cdto.setCustomerId(c.getCustomerId());
        cdto.setDocumentType(customerKycDto.getDocumentType());
        cdto.setFilePath(customerKycDto.getFilePath());

        try {
            customerAdminInterface.sendKycDetails(cdto);
            customerTicketNotificationInterface.SendAdminKycDetails(c.getCustomerId());
        } catch (Exception ex) {
            // log and continue
        }

        return kycDocument;
    }

    // 9. Get login history
    public List<CustomerLoginHistory> getLoginHistory(Long customerId) {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + customerId));
        return customerLoginHistoryRepository.findAllByCustomer(c);
    }
}
