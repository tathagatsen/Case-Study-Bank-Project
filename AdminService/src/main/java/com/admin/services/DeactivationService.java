package com.admin.services;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.dto.*;
import com.admin.feign.AccountFeignClientInterface;
import com.admin.feign.CustomerFeignClient;
import com.admin.feign.TransactionFeignClientInterface;
import com.admin.models.AdminActivityLog;
import com.admin.repositories.AdminActivityRepository;

import lombok.RequiredArgsConstructor;


@Service
public class DeactivationService {
	@Autowired
    private CustomerFeignClient customerClient;

    public boolean deactivateCustomer(Long customerId) {
        Boolean resBoolean= customerClient.deactivateCustomer(customerId);
        return resBoolean;
    }
}
