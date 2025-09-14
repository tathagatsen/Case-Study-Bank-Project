package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.dto.TransactionFlagDto;

@FeignClient(name = "admin-service", url="http://localhost:8086", path="/admin")
public interface AdminClientInterface {
	@PostMapping("/transactions/notify-flagged")
    void notifyFlaggedTransaction(@RequestBody TransactionFlagDto dto);

    @PostMapping("/transactions/notify-unflagged")
    void notifyUnflaggedTransaction(@RequestBody TransactionFlagDto dto);
}
