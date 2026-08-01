package com.drsanjitclinic.controller;

import com.drsanjitclinic.model.Payment;
import com.drsanjitclinic.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    // Records a fee payment.
    // NOTE: This is a simple record-keeping endpoint, NOT a real payment gateway.
    // For real online payments (UPI/Card), integrate Razorpay / Stripe / PayU here
    // and mark status SUCCESS only after the gateway confirms the transaction.
    @PostMapping
    public ResponseEntity<?> recordPayment(@RequestBody Payment payment) {
        if (payment.getPatientName() == null || payment.getPatientName().isBlank()
                || payment.getPhoneNumber() == null || payment.getPhoneNumber().isBlank()
                || payment.getAmount() == null || payment.getAmount() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Name, phone number and a valid amount are required"));
        }
        payment.setId(null);
        payment.setTransactionRef("DRSK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setStatus("SUCCESS"); // simulated success for demo purposes
        Payment saved = paymentRepository.save(payment);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Payment recorded successfully");
        response.put("transactionRef", saved.getTransactionRef());
        response.put("paymentId", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
