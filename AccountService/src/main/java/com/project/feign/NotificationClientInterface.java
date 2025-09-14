package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.dto.EmailDTO;

@FeignClient(name = "02-NotificationsModule", url = "http://localhost:8085/notifications")
public interface NotificationClientInterface {

    @PostMapping("/send-email")
    ResponseEntity<Void> sendEmail(@RequestBody EmailDTO email);
}
