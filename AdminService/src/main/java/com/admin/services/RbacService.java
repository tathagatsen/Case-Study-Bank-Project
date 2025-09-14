package com.admin.services;

import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class RbacService {

    // In-memory role store: userId -> list of roles
    private final Map<Long, List<String>> userRoles = new HashMap<>();


    public void assignRole(Long userId, String role) {
        userRoles
                .computeIfAbsent(userId, k -> new ArrayList<>())
                .add(role);
    }


    public String getUserRoles(Long userId) {
        List<String> roles = userRoles.getOrDefault(userId, Collections.emptyList());
        return String.join(",", roles);
    }
}
