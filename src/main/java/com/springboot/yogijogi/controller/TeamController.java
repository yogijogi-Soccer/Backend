package com.springboot.yogijogi.controller;

import com.springboot.yogijogi.dto.Team.*;
import com.springboot.yogijogi.entity.Member;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.repository.MemberRepository;
import com.springboot.yogijogi.service.Impl.TeamServiceImpl;
import com.springboot.yogijogi.service.TeamService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/team-api")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;



    private Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);


    @PostMapping("/create")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamResultDto> createTeam(HttpServletRequest request,
                                                    @RequestBody TeamProfileDto teamProfileDto) {
        TeamResultDto teamResultDto = teamService.createTeam(request,request.getHeader("X-AUTH-TOKEN"), teamProfileDto);
        if (teamResultDto.isSuccess()) {
            return new ResponseEntity<>(teamResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(teamResultDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/teamMoreInfo1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamResultDto> TeamMoreInfo1(HttpServletRequest request,
                                                    @RequestBody TeamMoreInfodDto1 teamMoreInfodDto1) {
        TeamResultDto teamResultDto = teamService.TeamMoreInfo1(request,request.getHeader("X-AUTH-TOKEN"), teamMoreInfodDto1);
        if (teamResultDto.isSuccess()) {
            return new ResponseEntity<>(teamResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(teamResultDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/teamMoreInfo2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamResultDto> TeamMoreInfo2(HttpServletRequest request,
                                                    @RequestBody TeamMoreInfodDto2 teamMoreInfodDto2) {
        TeamResultDto teamResultDto = teamService.TeamMoreInfo2(request,request.getHeader("X-AUTH-TOKEN"), teamMoreInfodDto2);
        if (teamResultDto.isSuccess()) {
            return new ResponseEntity<>(teamResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(teamResultDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/select-invite_code")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamInviteCodeDto> SelectTeamByInviteCode(HttpServletRequest request,
                                                       @RequestParam String invite_code) {
        TeamInviteCodeDto teamInviteCodeDto = teamService.SelectTeamByInviteCode(request,request.getHeader("X-AUTH-TOKEN"), invite_code);
        return ResponseEntity.status(HttpStatus.OK).body(teamInviteCodeDto);
    }

    @PostMapping("/join-invite_code")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamResultDto> JoinUpByInviteCode(HttpServletRequest request) {
        TeamResultDto teamResultDto = teamService.JoinUpByInviteCode(request,request.getHeader("X-AUTH-TOKEN"));
        return ResponseEntity.status(HttpStatus.OK).body(teamResultDto);
    }

    @GetMapping ("/join-up-select-team")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamJoinSelectDto> JoinUpSelectInfo(HttpServletRequest request,
                                                              @RequestParam Long id) {
        TeamJoinSelectDto teamJoinSelectDto = teamService.JoinUpSelectTeamInfo(request,request.getHeader("X-AUTH-TOKEN"), id);
        return ResponseEntity.status(HttpStatus.OK).body(teamJoinSelectDto);
    }

    @GetMapping ("/join-up-select-mamber")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamJoinSelectMemberDto> JoinUpSelectMemberInfo(HttpServletRequest request) {
        TeamJoinSelectMemberDto teamJoinSelectMemberDto1 = teamService.JoinUpSelectMemberInfo(request,request.getHeader("X-AUTH-TOKEN"));
        return ResponseEntity.status(HttpStatus.OK).body(teamJoinSelectMemberDto1);
    }

    @PostMapping ("/join-up-form")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamResultDto> JoinUp(HttpServletRequest request, TeamJoinDto teamJoin) {
        TeamResultDto teamResultDto = teamService.JoinUp(request,request.getHeader("X-AUTH-TOKEN"),teamJoin);
        return ResponseEntity.status(HttpStatus.OK).body(teamResultDto);
    }


}
