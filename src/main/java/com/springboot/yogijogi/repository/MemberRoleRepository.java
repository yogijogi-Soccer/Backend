package com.springboot.yogijogi.repository;

import com.springboot.yogijogi.entity.MemberRole;
import com.springboot.yogijogi.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole,Long> {

}
