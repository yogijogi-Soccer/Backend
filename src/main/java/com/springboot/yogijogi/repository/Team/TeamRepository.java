package com.springboot.yogijogi.repository.Team;

import com.springboot.yogijogi.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long>,TeamRepositoryCustom {
    Team findByInviteCode(String inviteCode);
}
