package com.drsanjitclinic.controller;

import com.drsanjitclinic.model.ContactMessage;
import com.drsanjitclinic.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @PostMapping
    public ResponseEntity<?> submitMessage(@RequestBody ContactMessage contactMessage) {
        if (contactMessage.getName() == null || contactMessage.getName().isBlank()
                || contactMessage.getPhoneNumber() == null || contactMessage.getPhoneNumber().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Name and phone number are required"));
        }
        contactMessage.setId(null);
        contactRepository.save(contactMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Thank you! We will get back to you soon."));
    }
}
