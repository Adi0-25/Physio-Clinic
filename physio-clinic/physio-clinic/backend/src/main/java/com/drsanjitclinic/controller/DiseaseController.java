package com.drsanjitclinic.controller;

import com.drsanjitclinic.model.Disease;
import com.drsanjitclinic.repository.DiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diseases")
public class DiseaseController {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @GetMapping
    public List<Disease> getAllDiseases() {
        return diseaseRepository.findAll();
    }

    @GetMapping("/{id}")
    public Disease getDiseaseById(@PathVariable Long id) {
        return diseaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Condition not found with id " + id));
    }
}
