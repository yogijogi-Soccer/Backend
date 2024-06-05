package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.Team.*;
import com.springboot.yogijogi.dto.Team.Join.TeamInviteCodeDto;
import com.springboot.yogijogi.dto.Team.Join.TeamJoinDto;
import com.springboot.yogijogi.dto.Team.Join.TeamJoinSelectDto;
import com.springboot.yogijogi.dto.Team.Join.TeamJoinSelectMemberDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface TeamService {
    TeamResultDto createTeam(HttpServletRequest request, String token, MultipartFile team_image, String team_Introduce, String team_name);

    TeamResultDto TeamMoreInfo1(HttpServletRequest request,String token, TeamMoreInfodDto1 teamMoreInfodDto1);
    TeamResultDto TeamMoreInfo2(HttpServletRequest request, String token, TeamMoreInfodDto2 teamMoreInfodDto2) ;
    TeamInviteCodeDto SelectTeamByInviteCode(HttpServletRequest request, String token, String invite_code);
    TeamResultDto JoinUpByInviteCode(HttpServletRequest request,String token);
    TeamJoinSelectDto JoinUpSelectTeamInfo(HttpServletRequest request, String token, Long id);
    TeamJoinSelectMemberDto JoinUpSelectMemberInfo(HttpServletRequest request, String token);
    TeamResultDto JoinUp(HttpServletRequest request, String token, TeamJoinDto teamJoinDto);


}
