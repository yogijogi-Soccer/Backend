package com.springboot.yogijogi.repository.JoinForm;


import com.springboot.yogijogi.dto.Team.TeamDetailJoinDto;
import com.springboot.yogijogi.entity.JoinForm;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JoinFormCustom {
    List<JoinForm> findPendingJoinFormsByTeam_TeamId(Long teamId);

}
