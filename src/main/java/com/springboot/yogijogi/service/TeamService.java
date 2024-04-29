package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.Team.*;

import javax.servlet.http.HttpServletRequest;

public interface TeamService {
    TeamResultDto createTeam(HttpServletRequest request,String token, TeamProfileDto teamProfileDto);
    TeamResultDto TeamMoreInfo1(HttpServletRequest request,String token, TeamMoreInfodDto1 teamMoreInfodDto1);
    TeamResultDto TeamMoreInfo2(HttpServletRequest request, String token, TeamMoreInfodDto2 teamMoreInfodDto2) ;
    TeamInviteCodeDto SelectTeamByInviteCode(HttpServletRequest request, String token,String invite_code);
    TeamResultDto JoinUpByInviteCode(HttpServletRequest request,String token);

    }
