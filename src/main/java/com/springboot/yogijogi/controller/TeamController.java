package com.springboot.yogijogi.controller;

import com.springboot.yogijogi.dto.Team.*;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.repository.MemberRepository;
import com.springboot.yogijogi.service.Impl.TeamServiceImpl;
import com.springboot.yogijogi.service.TeamService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/team-api")
public class TeamController {

    private final TeamService teamService;

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    private Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
    @Autowired
    private TeamController (TeamService teamService, JwtProvider jwtProvider, MemberRepository memberRepository){
        this.teamService = teamService;
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

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

    @PostMapping("/invite_code")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamInviteCodeDto> JoinInviteCode(HttpServletRequest request,
                                                       @RequestParam String invite_code) {
        TeamInviteCodeDto teamInviteCodeDto = teamService.JoinInviteCode(request.getHeader("X-AUTH-TOKEN"), invite_code);
        return ResponseEntity.status(HttpStatus.OK).body(teamInviteCodeDto);
    }

}
