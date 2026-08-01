package com.drsanjitclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(nullable = false, length = 100)
    private String patientName;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "payment_purpose", length = 100)
    private String paymentPurpose; // e.g. "Consultation Fee", "Session Fee - Back Pain"

    @Column(name = "payment_mode", length = 30)
    private String paymentMode; // UPI / CARD / CASH / NET_BANKING

    @Column(name = "transaction_ref", length = 100)
    private String transactionRef;

    @Column(length = 30)
    private String status = "INITIATED"; // INITIATED, SUCCESS, FAILED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
