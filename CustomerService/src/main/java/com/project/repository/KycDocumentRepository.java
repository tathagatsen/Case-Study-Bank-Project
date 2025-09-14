package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.Customer;
import com.project.model.KycDocument;

public interface KycDocumentRepository extends JpaRepository<KycDocument, Long> {
   // List<KycDocument> findByCustomerId(Long customerId);
	 List<KycDocument> findAllByCustomer(Customer customer);  // ✅ entity relation
	 List<KycDocument> findAllByStatus(String status);
	 List<KycDocument> findByCustomerCustomerId(Long customerId);
}