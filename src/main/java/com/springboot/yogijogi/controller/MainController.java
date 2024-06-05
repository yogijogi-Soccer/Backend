package com.springboot.yogijogi.controller;

import com.springboot.yogijogi.dto.Team.Search.TeamNameSearchDto;
import com.springboot.yogijogi.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/main-api")
@RequiredArgsConstructor
public class MainController {
    private final MainPageService mainPageService;



    @GetMapping("/search-team-name")
    public ResponseEntity<List<TeamNameSearchDto>>searchTeamName(String team_name){
        return ResponseEntity.ok(mainPageService.searchTeamName(team_name));
    }

    @GetMapping("/search-region")
    public ResponseEntity<List<TeamNameSearchDto>>searchRegion(String region){
        return ResponseEntity.ok(mainPageService.searchRegion(region));
    }

    @GetMapping("/search-gender")
    public ResponseEntity<List<TeamNameSearchDto>>searchGender(String gender){
        return ResponseEntity.ok(mainPageService.searchGender(gender));
    }

    @GetMapping("/search-day")
    public ResponseEntity<List<TeamNameSearchDto>>searchDay(String activity_days){
        return ResponseEntity.ok(mainPageService.searchDay(activity_days));
    }
    @GetMapping("/search-time")
    public ResponseEntity<List<TeamNameSearchDto>>searchTime(String time){
        return ResponseEntity.ok(mainPageService.searchTime(time));
    }

}
