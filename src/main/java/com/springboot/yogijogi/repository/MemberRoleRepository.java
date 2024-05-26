package com.springboot.yogijogi.repository;

import com.springboot.yogijogi.entity.MemberRole;
import com.springboot.yogijogi.entity.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole,Long> {
    @EntityGraph(attributePaths = {"member", "team", "role"})
    Optional<MemberRole> findById(Long id);
}
