package com.ofss.notifications.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Customer", url = "http://localhost:8082/customer")
public interface CustomerTicketFeign {

    @PutMapping("/support-ticket/resolve")
    void markTicketResolved(@RequestParam Long customerId, @RequestParam Long ticketId);
}