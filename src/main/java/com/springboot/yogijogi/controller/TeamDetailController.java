package com.springboot.yogijogi.controller;

import com.springboot.yogijogi.dto.Team.TeamPlayScheduleDto;
import com.springboot.yogijogi.dto.Team.TeamProfileDto;
import com.springboot.yogijogi.dto.Team.TeamResultDto;
import com.springboot.yogijogi.service.TeamDetailService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/teamDetail-api")
@RequiredArgsConstructor
public class TeamDetailController {

    private final TeamDetailService teamDetailService;


    @PostMapping("/play-registration")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamResultDto> TeamPlayRegistration(HttpServletRequest request,
                                                    @RequestBody TeamPlayScheduleDto teamPlayScheduleDto) {
        TeamResultDto teamResultDto = teamDetailService.TeamPlayRegistration(request,request.getHeader("X-AUTH-TOKEN"), teamPlayScheduleDto);
        if (teamResultDto.isSuccess()) {
            return new ResponseEntity<>(teamResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(teamResultDto, HttpStatus.BAD_REQUEST);
        }
    }


}
