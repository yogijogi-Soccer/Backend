package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.Team.TeamMoreInfodDto1;
import com.springboot.yogijogi.dto.Team.TeamMoreInfodDto2;
import com.springboot.yogijogi.dto.Team.TeamProfileDto;
import com.springboot.yogijogi.dto.Team.TeamResultDto;

import javax.servlet.http.HttpServletRequest;

public interface TeamService {
    TeamResultDto createTeam(HttpServletRequest request,String token, TeamProfileDto teamProfileDto);
    TeamResultDto TeamMoreInfo1(HttpServletRequest request,String token, TeamMoreInfodDto1 teamMoreInfodDto1);
    TeamResultDto TeamMoreInfo2(HttpServletRequest request, String token, TeamMoreInfodDto2 teamMoreInfodDto2) ;

    }
