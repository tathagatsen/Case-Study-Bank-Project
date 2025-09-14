package com.ofss.notifications.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofss.notifications.model.SupportTicket;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket,Long>{

	List<SupportTicket> findByCustomerId(Long customerId);

}
