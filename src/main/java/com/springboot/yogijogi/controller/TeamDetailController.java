package com.springboot.yogijogi.controller;

import com.springboot.yogijogi.dto.Team.*;
import com.springboot.yogijogi.service.TeamDetailService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
                                                              @RequestBody TeamPlayScheduleDto teamPlayScheduleDto,@RequestParam String formation_name) {
        TeamResultDto teamResultDto = teamDetailService.TeamPlayRegistration(request, request.getHeader("X-AUTH-TOKEN"), teamPlayScheduleDto,formation_name);
        if (teamResultDto.isSuccess()) {
            return new ResponseEntity<>(teamResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(teamResultDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/play-schedule")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamPlayScheduleDto> TeamPlaySchedule(HttpServletRequest request,
                                                                @RequestParam Long id) {
        TeamPlayScheduleDto scheduleDto = teamDetailService.TeamPlaySchedule(request, request.getHeader("X-AUTH-TOKEN"),id);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleDto);


    }

    @GetMapping("/join-player-select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<List<TeamDetailJoinDto>> TeamPlayJoinFormSelect(HttpServletRequest request,
                                                                    @RequestParam Long id) {
        List<TeamDetailJoinDto> teamDetailJoinDto = teamDetailService.TeamPlayJoinFormSelect(request, request.getHeader("X-AUTH-TOKEN"),id);
        return ResponseEntity.status(HttpStatus.OK).body(teamDetailJoinDto);

    }

    @GetMapping("/pomation-select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamPomatinDto> TeamPlayJoinFormSelect(HttpServletRequest request, String token,@RequestParam String position_name) {
        TeamPomatinDto teamPomatinDto = teamDetailService.TeamPomationSelectTest(request,token,position_name);
        return ResponseEntity.status(HttpStatus.OK).body(teamPomatinDto);

    }
    @PatchMapping("/join-approve")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<TeamResultDto> JoinFormApprove(HttpServletRequest request, @RequestBody TeamDetailJoinApproveDto teamDetailJoinApproveDto) {
        TeamResultDto teamResultDto = teamDetailService.JoinFormApprove(request ,teamDetailJoinApproveDto);

        return ResponseEntity.status(HttpStatus.OK).body(teamResultDto);

    }


}
