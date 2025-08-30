package com.project.service;

import java.net.Authenticator.RequestorType;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import com.project.dto.CustomerAdminDto;
import com.project.dto.CustomerDto;
import com.project.dto.CustomerKycDto;
import com.project.dto.CustomerSupportTicketDto;
import com.project.model.Customer;
import com.project.model.CustomerLoginHistory;
import com.project.model.KycDocument;
import com.project.model.SupportTicket;
import com.project.repository.CustomerKycRepository;
import com.project.repository.CustomerLoginHistoryRepository;
import com.project.repository.CustomerRepository;
import com.project.repository.CustomerSupportTicketRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	private CustomerLoginHistoryRepository customerLoginHistoryRepository;
	
	@Autowired
	private CustomerSupportTicketRepository customerSupportTicketRepository;
	
	@Autowired
	private CustomerKycRepository customerKycRepository;
	//1
	public Customer registerCustomer(CustomerDto customerDto) {
		if (repository.findById(customerDto.getCustomerId()) == null) {
			Customer c = new Customer();
			//send to admin service remaining
			
			c.setActive(true);
			c.setCustomerId(customerDto.getCustomerId());
			c.setName(customerDto.getName());
			c.setEmail(customerDto.getName());
			c.setPhone(customerDto.getPhone());
			c.setAddress(customerDto.getAddress());
			c.setDob(customerDto.getDob());
			repository.save(c);
		} else {
			throw new RuntimeException("Already Registered!!");
		}
		return null;
	}
	
//	2
	public Customer getCustomerDetails(Long custId) {
		Optional<Customer> c = repository.findById(custId);
		return c.get();
	}
//3
	public Customer deactivateCustomer(Long customerId) {
		//get AdminCustomerDto response from admin service and set isActive to false
		//Also send notification to customer id regarding setting Customer inactive.
		return null;
	}
//4
	public Long loginCustomer(Long custId) {
		Optional<Customer> c=repository.findById(custId);
		CustomerLoginHistory custLoginHistory=new CustomerLoginHistory();
		custLoginHistory.setCustomer(c.get());
		custLoginHistory.setLoginTime(LocalDateTime.now());
//		custLoginHistory.setIpAddress(getClientIpAddress(null));
		customerLoginHistoryRepository.save(custLoginHistory);
		List<CustomerLoginHistory> list=customerLoginHistoryRepository.findAllByCustomer(c.get());
		c.get().setLoginHistories(list);
		repository.save(c.get());
		return null;
	}
//	public String getClientIpAddress(HttpServletRequest request) {
//        String ipAddress = request.getRemoteAddr();
//        return ipAddress;
		//this to be implemented in user while passing through feign
//	String ipAddress = getClientIpAddress(request);
//    return customerClient.loginCustomer(custId, ipAddress);
//    }
	
//	5
	public String createSupportTicket(CustomerSupportTicketDto customerSupportTicketDto) {
		// TODO Auto-generated method stub
		Optional<Customer> c=repository.findById(customerSupportTicketDto.getCustomerId());
		SupportTicket supportTicket=new SupportTicket();
		supportTicket.setCreatedAt(LocalDateTime.now());
		supportTicket.setCustomer(c.get());
		supportTicket.setDescription(customerSupportTicketDto.getDescription());
		supportTicket.setStatus("OPEN");
		supportTicket.setSubject(customerSupportTicketDto.getSubject());
		customerSupportTicketRepository.save(supportTicket);
		
		List<SupportTicket> list=customerSupportTicketRepository.findAllByCustomer(c.get());
		c.get().setSupportTickets(list);
		repository.save(c.get());
		return "Support ticket Created";
	}

	public List<SupportTicket> listSupportTickets(Long customerId) {
		// TODO Auto-generated method stub
		Optional<Customer> c=repository.findById(customerId);
		return customerSupportTicketRepository.findAllByCustomer(c.get());
	}

	public SupportTicket getSupportTicketDetails(Long customerId, Long ticketId) {
		// TODO Auto-generated method stub
		return customerSupportTicketRepository.findByIdAndCustomer(customerId,ticketId);
	}
	
//6
	public KycDocument uploadKycDocument(CustomerKycDto customerKycDto) {
		// TODO Auto-generated method stub
		Optional<Customer> c=repository.findById(customerKycDto.getCustomerId());
		//send to admin service file path and other info
		KycDocument kycDocument=new KycDocument();
		kycDocument.setDocumentType(customerKycDto.getDocumentType());
		kycDocument.setCustomer(c.get());
		kycDocument.setFilePath(customerKycDto.getFilePath());
		customerKycRepository.save(kycDocument);
		List<KycDocument> list=customerKycRepository.findAllByCustomer(c.get());
		c.get().setKycDocuments(list);
		return null;
	}
	
	
	
	
	
	
	
}
