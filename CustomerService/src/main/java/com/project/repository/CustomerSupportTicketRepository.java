package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.model.Customer;
import com.project.model.SupportTicket;
@Repository
public interface CustomerSupportTicketRepository extends JpaRepository<SupportTicket, Long>{

	List<SupportTicket> findAllByCustomer(Customer customerId);
	
	
	@Query(value="SELECT * FROM SupportTicket st WHERE st.ticketId= ?2 AND st.customerId= ?1",nativeQuery=true)
	SupportTicket findByIdAndCustomer(Long customerId, Long ticketId);

	//Optional<SupportTicket> findByCustomerIdAndTicketId(Long customerId, Long ticketId);
	Optional<SupportTicket> findByCustomerCustomerIdAndTicketId(Long customerId, Long ticketId);

}
