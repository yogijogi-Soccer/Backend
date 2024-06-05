package com.springboot.yogijogi.repository.JoinForm;


import com.springboot.yogijogi.entity.JoinForm;

import java.util.List;

public interface JoinFormCustom {
    List<JoinForm> findPendingJoinFormsByTeam_TeamId(Long teamId);

}
