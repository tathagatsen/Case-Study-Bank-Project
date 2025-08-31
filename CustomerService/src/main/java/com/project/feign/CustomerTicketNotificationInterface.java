package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.dto.TicketNotificationDto;

@FeignClient("Notification")
public interface CustomerTicketNotificationInterface {

	
	@PostMapping("/sent-admin-kyc")
	public void SendAdminKycDetails(@RequestParam Long customerId);
	
	@PostMapping("/notify-ticket")
	void sendTicketNotification(@RequestBody TicketNotificationDto dto);
	
}
