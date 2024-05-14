package com.springboot.yogijogi.dto.Team;

import com.springboot.yogijogi.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamDetailJoinDto {
    private String name;
    private String checked;
    private List<String> position;
}
