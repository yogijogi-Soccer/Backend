package com.springboot.yogijogi.dto.Team;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamJoinSelectMemberDto {
    private String name;
    private Long birth_date;
    private boolean has_experience;
    private List<String> available_days ;
    private String available_time_start;  // 시작 시간;
    private String  available_time_end;  // 종료 시간
    private List<String> position;
}