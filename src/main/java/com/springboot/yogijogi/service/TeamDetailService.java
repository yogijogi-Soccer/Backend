package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.Team.TeamDetailJoinDto;
import com.springboot.yogijogi.dto.Team.TeamPlayScheduleDto;
import com.springboot.yogijogi.dto.Team.TeamPomatinDto;
import com.springboot.yogijogi.dto.Team.TeamResultDto;
import com.springboot.yogijogi.entity.JoinForm;

import javax.servlet.http.HttpServletRequest;

public interface TeamDetailService {
    TeamResultDto TeamPlayRegistration(HttpServletRequest request, String token, TeamPlayScheduleDto teamPlayScheduleDto,String position_name);
    TeamPlayScheduleDto TeamPlaySchedule(HttpServletRequest request,String token, Long id);
    TeamDetailJoinDto TeamPlayJoinFormSelect(HttpServletRequest request, String token, Long id);
    TeamPomatinDto TeamPomationSelectTest(HttpServletRequest request, String token,String position_name);
}

