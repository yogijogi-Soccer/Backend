package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.Team.Search.TeamNameSearchDto;

import java.util.List;

public interface MainPageService {
    List<TeamNameSearchDto> searchTeamName(String team_namem_name);
    List<TeamNameSearchDto> searchRegion(String region);
    List<TeamNameSearchDto> searchGender(String gender);
    List<TeamNameSearchDto> searchDay(String day);
    List<TeamNameSearchDto> searchTime(String time);
}
