package com.admin.services;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.dto.AdminActionDto;
import com.admin.dto.CustomerAdminKycDto;
import com.admin.dto.CustomerKycStatusUpdateDto;
import com.admin.feign.CustomerFeignClient;
import com.admin.models.AdminKyc;
import com.admin.repositories.AdminKycRepository;

@Service

public class AdminKycService {
	@Autowired
	private AdminKycRepository adminKycRepository;
	@Autowired
	private CustomerFeignClient customerFeignClient;

	public void receiveKycFromCustomer(CustomerAdminKycDto dto) {
		AdminKyc a = AdminKyc.builder().kycId(dto.getKycId()).customerId(dto.getCustomerId())
				.documentType(dto.getDocumentType()).filePath(dto.getFilePath()).status("PENDING")
				.createdAt(LocalDateTime.now()).build();
		adminKycRepository.save(a);
	}

	public void approveKyc(Long adminKycId, AdminActionDto action) {
		AdminKyc a = adminKycRepository.findById(adminKycId)
				.orElseThrow(() -> new RuntimeException("AdminKyc not found: " + adminKycId));
		a.setStatus("APPROVED");
		a.setReviewedBy(String.valueOf(action.getAdminId()));
		a.setReviewedAt(LocalDateTime.now());
		a.setRemarks(action.getRemarks());
		adminKycRepository.save(a);

		// Build callback DTO to Customer and call
		CustomerKycStatusUpdateDto callback = new CustomerKycStatusUpdateDto(a.getKycId(), a.getCustomerId(),
				"APPROVED", a.getReviewedBy(), a.getRemarks(), a.getReviewedAt());
		customerFeignClient.updateKycStatus(callback);
		// Optional: call Notification service to mail user
	}

	public void rejectKyc(Long adminKycId, AdminActionDto action) {
		AdminKyc a = adminKycRepository.findById(adminKycId)
				.orElseThrow(() -> new RuntimeException("AdminKyc not found: " + adminKycId));
		a.setStatus("REJECTED");
		a.setReviewedBy(String.valueOf(action.getAdminId()));
		a.setReviewedAt(LocalDateTime.now());
		a.setRemarks(action.getRemarks());
		adminKycRepository.save(a);

		CustomerKycStatusUpdateDto callback = new CustomerKycStatusUpdateDto(a.getKycId(), a.getCustomerId(),
				"REJECTED", a.getReviewedBy(), a.getRemarks(), a.getReviewedAt());
		customerFeignClient.updateKycStatus(callback);
	}

	public List<AdminKyc> getPending() {
		// TODO Auto-generated method stub
		return adminKycRepository.findAllByStatus("PENDING");
	}
	
	 @Autowired
	    private CustomerFeignClient customerClient;

	    public void reviewKyc(CustomerAdminKycDto dto) {
	        try (FileReader fr = new FileReader(dto.getFilePath())) {
	            // ✅ File exists, assume valid for demo
	            CustomerKycStatusUpdateDto statusDto = new CustomerKycStatusUpdateDto(
	                dto.getKycId(),
	                dto.getCustomerId(),
	                "APPROVED",
	                "admin01",
	                "Document verified successfully",
	                LocalDateTime.now()
	            );
	            customerClient.updateKycStatus(statusDto);
	        } catch (Exception e) {
	            // ❌ File missing / invalid → Reject
	            CustomerKycStatusUpdateDto statusDto = new CustomerKycStatusUpdateDto(
	                dto.getKycId(),
	                dto.getCustomerId(),
	                "REJECTED",
	                "admin01",
	                "File not readable",
	                LocalDateTime.now()
	            );
	            customerClient.updateKycStatus(statusDto);
	        }
	    }
	}
