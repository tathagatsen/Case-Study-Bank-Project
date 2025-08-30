package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.Customer;
import com.project.model.KycDocument;

public interface CustomerKycRepository extends JpaRepository<KycDocument, Long>{

	List<KycDocument> findAllByCustomer(Customer customer);

}
