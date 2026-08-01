package com.drsanjitclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diseases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String shortDescription;

    @Column(length = 2000)
    private String treatmentApproach;

    @Column(name = "min_recovery_days")
    private Integer minRecoveryDays;

    @Column(name = "max_recovery_days")
    private Integer maxRecoveryDays;

    @Column(name = "sessions_per_week")
    private Integer sessionsPerWeek;

    @Column(name = "consultation_fee")
    private Double consultationFee;

    @Column(name = "session_fee")
    private Double sessionFee;

    @Column(length = 50)
    private String severityLevel; // Mild / Moderate / Severe

}
