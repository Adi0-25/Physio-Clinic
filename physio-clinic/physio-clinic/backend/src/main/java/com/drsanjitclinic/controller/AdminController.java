package com.drsanjitclinic.controller;

import com.drsanjitclinic.model.Appointment;
import com.drsanjitclinic.model.ContactMessage;
import com.drsanjitclinic.model.Payment;
import com.drsanjitclinic.repository.AppointmentRepository;
import com.drsanjitclinic.repository.ContactRepository;
import com.drsanjitclinic.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Simple admin endpoints so the clinic owner can see who booked, who paid, and who messaged.
 * Protected with a shared secret key (set ADMIN_KEY as an environment variable) sent via the
 * "X-Admin-Key" request header. This is intentionally lightweight for a small clinic site.
 * For a production system with multiple staff logins, replace this with Spring Security +
 * username/password or JWT based authentication.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Value("${admin.secret.key}")
    private String adminKey;

    private boolean isUnauthorized(String providedKey) {
        return providedKey == null || !providedKey.equals(adminKey);
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> getAppointments(@RequestHeader(value = "X-Admin-Key", required = false) String key) {
        if (isUnauthorized(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid admin key"));
        }
        List<Appointment> appointments = appointmentRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/payments")
    public ResponseEntity<?> getPayments(@RequestHeader(value = "X-Admin-Key", required = false) String key) {
        if (isUnauthorized(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid admin key"));
        }
        List<Payment> payments = paymentRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@RequestHeader(value = "X-Admin-Key", required = false) String key) {
        if (isUnauthorized(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid admin key"));
        }
        List<ContactMessage> messages = contactRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(@RequestHeader(value = "X-Admin-Key", required = false) String key) {
        if (isUnauthorized(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid admin key"));
        }
        double totalRevenue = paymentRepository.findAll().stream()
                .filter(p -> "SUCCESS".equals(p.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();
        Map<String, Object> summary = Map.of(
                "totalAppointments", appointmentRepository.count(),
                "totalPayments", paymentRepository.count(),
                "totalRevenue", totalRevenue,
                "totalMessages", contactRepository.count()
        );
        return ResponseEntity.ok(summary);
    }
}
