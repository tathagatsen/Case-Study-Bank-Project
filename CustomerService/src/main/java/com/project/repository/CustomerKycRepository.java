package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Customer;
import com.project.model.KycDocument;
@Repository
public interface CustomerKycRepository extends JpaRepository<KycDocument, Long>{

	List<KycDocument> findAllByCustomer(Customer customer);

}
