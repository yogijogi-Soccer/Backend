package com.springboot.yogijogi.dto.Team.Join;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamJoinSelectDto {
    private String team_name;
    private String team_introduce;
    private String team_image;
    private int member_num;
    private String dues;
    private List<String> activity_days;
    private String region;
    private String age;
    private List<String> activity_time;
    private String level;
}
