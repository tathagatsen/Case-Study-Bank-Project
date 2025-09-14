package com.ofss.notifications.model;
import lombok.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "SUPPORT_TICKET_NM")
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    private Long customerId;

    private String email;

    private String subject;

    @Column(length = 2000)
    private String description;

    private String status; // OPEN, IN_PROGRESS, RESOLVED, CLOSED

    private LocalDateTime createdAt;
}
