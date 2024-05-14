package com.springboot.yogijogi.repository;

import com.springboot.yogijogi.entity.Pomation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PomationRepository extends JpaRepository<Pomation,Long> {
    Pomation findByFormationName(String name);
}
