package com.springboot.yogijogi.dto.Team;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamNameSearchDto {
    private String team_name;
    private String age;
    private List<String> activity_time;
    private List<String> activity_days;
    private String gender;
    private String region;
    private String town;
}
