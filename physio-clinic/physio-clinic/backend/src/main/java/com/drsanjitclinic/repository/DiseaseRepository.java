package com.drsanjitclinic.repository;

import com.drsanjitclinic.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
}
