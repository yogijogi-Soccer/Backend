package com.springboot.yogijogi.controller;

import com.springboot.yogijogi.dto.Announcement.AnnouncementDetailDto;
import com.springboot.yogijogi.dto.Announcement.AnnouncementDto;
import com.springboot.yogijogi.dto.Announcement.AnnouncementSelectDto;
import com.springboot.yogijogi.dto.Team.*;

import com.springboot.yogijogi.dto.Team.Join.TeamDetailJoinApproveDto;
import com.springboot.yogijogi.dto.Team.Join.TeamDetailJoinDto;
import com.springboot.yogijogi.service.AnnouncementService;
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
    private final AnnouncementService announcementService;


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



    @PostMapping("/annoncement-save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<AnnouncementDto> saveAnnouncement(HttpServletRequest request, @RequestBody AnnouncementDto announcementDto) {
        AnnouncementDto savedAnnouncement = announcementService.saveAnnouncement(request, announcementDto);
        return ResponseEntity.status(HttpStatus.OK).body(savedAnnouncement);
    }

    @PutMapping("/annoncement-update/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<AnnouncementDto> updateAnnouncement(HttpServletRequest request, @PathVariable Long id, @RequestBody AnnouncementDto announcementDto) {
        AnnouncementDto updatedAnnouncement = announcementService.updateAnnouncement(request, id, announcementDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAnnouncement);
    }

    @DeleteMapping("/annoncement-delete/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Void> deleteAnnouncement(HttpServletRequest request, @PathVariable Long id) {
        announcementService.deleteAnnouncement(request, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/annoncement-select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<List<AnnouncementSelectDto>> SelectAnnouncement(HttpServletRequest request, @RequestParam Long id) {
        List<AnnouncementSelectDto> announcementSelectDto = announcementService.selectAnnouncementList(request,id);
        return ResponseEntity.status(HttpStatus.OK).body(announcementSelectDto);

    }
    @GetMapping("/annoncement-detail-select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<AnnouncementDetailDto> selectAnnouncementDetail(HttpServletRequest request, @RequestParam Long teamId, Long announcementId ) {
        AnnouncementDetailDto announcementDetailDto = announcementService.selectAnnouncementDetail(request,teamId,announcementId);
        return ResponseEntity.status(HttpStatus.OK).body(announcementDetailDto);

    }

}
