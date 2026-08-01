package com.drsanjitclinic.controller;

import com.drsanjitclinic.model.Appointment;
import com.drsanjitclinic.repository.AppointmentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Book a new consultation appointment
    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {
        if (appointment.getPatientName() == null || appointment.getPatientName().isBlank()
                || appointment.getPhoneNumber() == null || appointment.getPhoneNumber().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Name and phone number are required"));
        }
        appointment.setId(null);
        appointment.setStatus("PENDING");
        Appointment saved = appointmentRepository.save(appointment);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Appointment request received. Our clinic will confirm shortly.");
        response.put("appointmentId", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
