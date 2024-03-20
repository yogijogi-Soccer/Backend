package com.springboot.yogijogi.repository;

import com.springboot.yogijogi.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long> {
}
