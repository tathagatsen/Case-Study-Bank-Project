package com.ofss.notifications.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofss.notifications.model.Notifications;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
	
//	Optional findById(Integer id);
//	
//	List<Notifications> findAll();
	

}
