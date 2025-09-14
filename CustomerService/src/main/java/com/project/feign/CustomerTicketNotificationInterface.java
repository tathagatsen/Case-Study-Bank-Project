package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.dto.TicketNotificationDto;

@FeignClient(name = "02-NotificationsModule",url = "http://localhost:8085/notifications")
public interface CustomerTicketNotificationInterface {

	
	@PostMapping("/sent-admin-kyc")
	public void sendAdminKycDetails(@RequestParam Long customerId, @RequestParam String email);
	
	@PostMapping("/notify-ticket")
	void sendTicketNotification(@RequestBody TicketNotificationDto dto);
	
}
