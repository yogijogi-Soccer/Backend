package com.springboot.yogijogi.service.Impl;

import com.springboot.yogijogi.dto.Team.Search.TeamNameSearchDto;
import com.springboot.yogijogi.entity.Team;
import com.springboot.yogijogi.repository.Team.TeamRepository;
import com.springboot.yogijogi.service.MainPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainPageServiceImpl implements MainPageService {

    private Logger logger = LoggerFactory.getLogger(MainPageServiceImpl.class);

    private final TeamRepository teamRepository;

    @Autowired
    public MainPageServiceImpl( TeamRepository teamRepository){

        this.teamRepository = teamRepository;
    }

    @Override
    public List<TeamNameSearchDto> searchTeamName(String team_name) {
        if(team_name == null){
            team_name= "";
        }
        List<Team> teams = teamRepository.searchTeam(team_name);

        return teams.stream().map(team ->{
            TeamNameSearchDto teamNameSearchDto = new TeamNameSearchDto();
            teamNameSearchDto.setTeam_name(team.getTeam_name());
            teamNameSearchDto.setAge(team.getAge());
            teamNameSearchDto.setActivity_time(team.getActivity_time());
            teamNameSearchDto.setActivity_days(team.getActivity_days());
            teamNameSearchDto.setGender(team.getGender());
            teamNameSearchDto.setRegion(team.getRegion());
            teamNameSearchDto.setTown(team.getTown());
            teamNameSearchDto.setTeam_imageUrl(team.getTeam_imageUrl());
            return teamNameSearchDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TeamNameSearchDto> searchRegion(String region) {
        if (region == null) {
            region = "";
        }logger.info("[region] region : {}",region);
        List<Team> teams = teamRepository.searchRegion(region);

        return teams.stream().map(team -> {
            TeamNameSearchDto teamNameSearchDto = new TeamNameSearchDto();
            teamNameSearchDto.setTeam_name(team.getTeam_name());
            teamNameSearchDto.setAge(team.getAge());
            teamNameSearchDto.setActivity_time(team.getActivity_time());
            teamNameSearchDto.setActivity_days(team.getActivity_days());
            teamNameSearchDto.setGender(team.getGender());
            teamNameSearchDto.setRegion(team.getRegion());
            teamNameSearchDto.setTown(team.getTown());
            teamNameSearchDto.setTeam_imageUrl(team.getTeam_imageUrl());
            return teamNameSearchDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TeamNameSearchDto> searchGender(String gender) {
        if (gender == null) {
            gender = "";
        }logger.info("[gender] gender : {}",gender);
        List<Team> teams = teamRepository.searchGender(gender);

        return teams.stream().map(team -> {
            TeamNameSearchDto teamNameSearchDto = new TeamNameSearchDto();
            teamNameSearchDto.setTeam_name(team.getTeam_name());
            teamNameSearchDto.setAge(team.getAge());
            teamNameSearchDto.setActivity_time(team.getActivity_time());
            teamNameSearchDto.setActivity_days(team.getActivity_days());
            teamNameSearchDto.setGender(team.getGender());
            teamNameSearchDto.setRegion(team.getRegion());
            teamNameSearchDto.setTown(team.getTown());
            teamNameSearchDto.setTeam_imageUrl(team.getTeam_imageUrl());
            return teamNameSearchDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TeamNameSearchDto> searchDay(String activity_days) {
        if (activity_days == null) {
            activity_days = "";
        }
        List<Team> teams = teamRepository.searchDay(activity_days);

        return teams.stream().map(team -> {
            TeamNameSearchDto teamNameSearchDto = new TeamNameSearchDto();
            teamNameSearchDto.setTeam_name(team.getTeam_name());
            teamNameSearchDto.setAge(team.getAge());
            teamNameSearchDto.setActivity_time(team.getActivity_time());
            teamNameSearchDto.setActivity_days(team.getActivity_days());
            teamNameSearchDto.setGender(team.getGender());
            teamNameSearchDto.setRegion(team.getRegion());
            teamNameSearchDto.setTown(team.getTown());
            teamNameSearchDto.setTeam_imageUrl(team.getTeam_imageUrl());
            return teamNameSearchDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TeamNameSearchDto> searchTime(String activity_time) {
        if (activity_time == null) {
            activity_time = "";
        }
        List<Team> teams = teamRepository.searchDay(activity_time);

        return teams.stream().map(team -> {
            TeamNameSearchDto teamNameSearchDto = new TeamNameSearchDto();
            teamNameSearchDto.setTeam_name(team.getTeam_name());
            teamNameSearchDto.setAge(team.getAge());
            teamNameSearchDto.setActivity_time(team.getActivity_time());
            teamNameSearchDto.setActivity_days(team.getActivity_days());
            teamNameSearchDto.setGender(team.getGender());
            teamNameSearchDto.setRegion(team.getRegion());
            teamNameSearchDto.setTown(team.getTown());
            teamNameSearchDto.setTeam_imageUrl(team.getTeam_imageUrl());
            return teamNameSearchDto;
        }).collect(Collectors.toList());
    }
}
