package com.admin.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String roleId;

    @Enumerated(EnumType.STRING)
    private RoleType roleName; // ADMIN, SUPPORT, AUDITOR

    private String description;
}

