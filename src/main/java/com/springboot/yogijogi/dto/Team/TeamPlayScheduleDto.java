package com.springboot.yogijogi.dto.Team;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamPlayScheduleDto {
    private String address;

    private String addressDetail;

    private int month;

    private int day;

    private int hour;

    private int minute;

    private String  opposingTeam;

    private String tactics;

    List<String> todayPositions;
}
