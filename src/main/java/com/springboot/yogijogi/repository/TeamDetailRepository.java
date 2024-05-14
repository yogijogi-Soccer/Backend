package com.springboot.yogijogi.repository;

import com.springboot.yogijogi.entity.Team;
import com.springboot.yogijogi.entity.TeamDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamDetailRepository extends JpaRepository<TeamDetail,Long> {
    TeamDetail findByDetailTeamId(Long id);
}
