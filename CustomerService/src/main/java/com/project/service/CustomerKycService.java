package com.project.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.project.feign.*;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.*;

import com.project.model.*;

import com.project.exception.ResourceNotFound;
import com.project.api.ApiResponse;
import com.project.feign.CustomerAccountInterface;
//import com.project.feign.CustomerAdminInterface;
import com.project.feign.CustomerTicketNotificationInterface;
import com.project.repository.CustomerKycRepository;
import com.project.repository.CustomerLoginHistoryRepository;
import com.project.repository.CustomerRepository;
import com.project.repository.CustomerSupportTicketRepository;
import com.project.repository.KycDocumentRepository;


@Service
public class CustomerKycService {

    @Autowired
	private final CustomerRepository customerRepository;
    @Autowired
    private KycDocumentRepository repo;

    @Autowired
    private AdminFeignClient adminClient;

    CustomerKycService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

//    public KycDocument uploadKyc(CustomerKycDto dto) {
//        KycDocument doc = new KycDocument();
//        doc.setCustomerId(dto.getCustomerId());
//        doc.setDocumentType(dto.getDocumentType());
//        doc.setFilePath(dto.getFilePath());
//        doc.setStatus("PENDING");
//        doc.setUploadedAt(LocalDateTime.now());
//
//        KycDocument saved = repo.save(doc);
//
//        // Send request to Admin for review
//        CustomerAdminKycDto adminDto = new CustomerAdminKycDto(
//            saved.getKycId(),
//            saved.getCustomerId(),
//            saved.getDocumentType(),
//            saved.getFilePath(),
//            saved.getUploadedAt()
//        );
//        adminClient.sendKycRequest(adminDto);
//
//        return saved;
//    }
//
//    public void updateKycStatus(CustomerKycStatusUpdateDto dto) {
//        KycDocument doc = repo.findById(dto.getKycId())
//                .orElseThrow(() -> new RuntimeException("KYC not found"));
//        doc.setStatus(dto.getStatus());
//        doc.setReviewedBy(dto.getReviewedBy());
//        doc.setRemarks(dto.getRemarks());
//        doc.setReviewedAt(LocalDateTime.now());
//        repo.save(doc);
//    }
//
//	public List<Customer> listAllCustomers() {
//		// TODO Auto-generated method stub
//		return customerRepository.findAll();
//	}
    
//    public KycDocument uploadKyc(CustomerKycDto dto) {
//        Customer customer = customerRepository.findById(dto.getCustomerId())
//            .orElseThrow(() -> new RuntimeException("Customer not found: " + dto.getCustomerId()));
//        try {
//            // Create directory if not exists
//            Path uploadDir = Paths.get("uploads/kyc/" + customer.getCustomerId());
//            if (!Files.exists(uploadDir)) {
//                Files.createDirectories(uploadDir);
//            }
//            // Build file path
//            Path filePath = uploadDir.resolve(uploadDir);
//            // Save file to local system
//            Files.write(filePath, dto.getData());
//            // Store only the path in DB
////            KycDocument doc = KycDocument.builder()
////                    .customer(customer)
////                    .documentType(documentType)
////                    .fileName(fileName)
////                    .fileContentType(contentType)
////                    .filePath(filePath.toString())
////                    .status(KycDocument.Status.PENDING)
////                    .uploadedAt(Instant.now())
////                    .build();
//            KycDocument document=new KycDocument();
//            document.getCustomer(customer);
//            document.getDocumentType(dto.getDocumentType());
//            
//            doc = kycRepository.save(doc);
//            return toDTO(doc);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to save file locally", e);
//        }
//        KycDocument doc = new KycDocument();
//        doc.setCustomer(customer);                      // ✅ set relation
//        doc.setDocumentType(dto.getDocumentType());
//        doc.setFilePath(dto.getFilePath());
//        doc.setStatus("PENDING");
//        doc.setUploadedAt(LocalDateTime.now());
//        
//        KycDocument saved = repo.save(doc);
//
//        // Send request to Admin for review
//        CustomerAdminKycDto adminDto = new CustomerAdminKycDto(
//            saved.getKycId(),
//            saved.getCustomer().getCustomerId(),        // ✅ extract ID from relation
//            saved.getDocumentType(),
//            saved.getFilePath(),
//            saved.getUploadedAt()
//        );
//        adminClient.receiveKyc(adminDto);
//
//        return saved;
//    }
    		
    public KycDocument uploadKyc(CustomerKycDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found: " + dto.getCustomerId()));
        try {
            // Create directory if not exists
            Path uploadDir = Paths.get("uploads/kyc/" + customer.getCustomerId());
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Build file path (use original file name or timestamp)
            String fileName = dto.getDocumentType() + "_" + System.currentTimeMillis() + ".pdf";
            Path filePath = uploadDir.resolve(fileName);

            // Save file to local system
            Files.write(filePath, dto.getData());

            // Create KycDocument entity
            KycDocument doc = new KycDocument();
            doc.setCustomer(customer);               // ✅ correct setter
            doc.setDocumentType(dto.getDocumentType());
            doc.setFilePath(filePath.toString());    // ✅ store actual path
            doc.setStatus("PENDING");
            doc.setUploadedAt(LocalDateTime.now());

            KycDocument saved = repo.save(doc);

            // Send request to Admin for review
            CustomerAdminKycDto adminDto = new CustomerAdminKycDto(
                saved.getKycId(),
                saved.getCustomer().getCustomerId(), // ✅ extract ID from relation
                saved.getDocumentType(),
                saved.getFilePath(),
                saved.getUploadedAt()
            );
            adminClient.receiveKyc(adminDto);

            return saved;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file locally", e);
        }
    }

    public List<KycDocumentDto> getAllKycForCustomer(Long customerId) {
        // Fetch all KYC documents for the customer
        List<KycDocument> list = repo.findByCustomerCustomerId(customerId);
        
        System.out.println(list);
        // Map entities to DTOs
        return list.stream()
                   .map(kyc -> new KycDocumentDto(
                       kyc.getKycId(),
                       kyc.getDocumentType(),
                       kyc.getRemarks(),
                       kyc.getFilePath(),
                       kyc.getStatus(),
                       kyc.getUploadedAt()
                   ))
                   .collect(Collectors.toList());
    }
    
    public List<KycDocument> getAllKyc() {
    	return repo.findAll();
    }
    
public void updateKycStatus(CustomerKycStatusUpdateDto dto) {
    KycDocument doc = repo.findById(dto.getKycId())
            .orElseThrow(() -> new RuntimeException("KYC not found"));
    doc.setStatus(dto.getStatus());
    doc.setReviewedBy(dto.getReviewedBy());
    doc.setRemarks(dto.getRemarks());
    doc.setReviewedAt(LocalDateTime.now());
    repo.save(doc);
}

public List<Customer> listAllCustomers() {
	// TODO Auto-generated method stub
	List<Customer> customers = customerRepository.findAll();
	return customers;
}
}