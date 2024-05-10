package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.Team.TeamPlayScheduleDto;
import com.springboot.yogijogi.dto.Team.TeamResultDto;

import javax.servlet.http.HttpServletRequest;

public interface TeamDetailService {
    TeamResultDto TeamPlayRegistration(HttpServletRequest request, String token, TeamPlayScheduleDto teamPlayScheduleDto);

}
