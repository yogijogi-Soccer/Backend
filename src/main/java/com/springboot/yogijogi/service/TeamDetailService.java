package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.Team.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TeamDetailService {
    TeamResultDto TeamPlayRegistration(HttpServletRequest request, String token, TeamPlayScheduleDto teamPlayScheduleDto,String position_name);
    TeamPlayScheduleDto TeamPlaySchedule(HttpServletRequest request,String token, Long id);
    List<TeamDetailJoinDto> TeamPlayJoinFormSelect(HttpServletRequest request, String token, Long id);
    TeamPomatinDto TeamPomationSelectTest(HttpServletRequest request, String token,String position_name);
    TeamResultDto JoinFormApprove(HttpServletRequest request, TeamDetailJoinApproveDto teamDetailJoinApproveDto);
}

