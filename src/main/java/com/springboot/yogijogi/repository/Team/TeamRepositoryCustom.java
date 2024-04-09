package com.springboot.yogijogi.repository.Team;

import com.springboot.yogijogi.entity.Team;

import java.util.List;

public interface TeamRepositoryCustom {
    List<Team> searchTeam(String team_name);
    List<Team> searchRegion(String region);
    List<Team> searchGender(String gender);
    List<Team> searchDay(String activity_days);
    List<Team> searchTime(String time);

}
