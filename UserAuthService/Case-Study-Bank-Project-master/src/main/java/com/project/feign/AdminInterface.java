package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.dto.AdminRequestDto;

@FeignClient(name="admin-server",url = "http://localhost:8086",path = "/admin/users")
public interface AdminInterface {
	@PostMapping
    public ResponseEntity<AdminRequestDto> createAdmin(@RequestBody AdminRequestDto adminRequestDto);
}
