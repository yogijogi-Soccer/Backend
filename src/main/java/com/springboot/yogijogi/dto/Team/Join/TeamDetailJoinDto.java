package com.springboot.yogijogi.dto.Team.Join;

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
    private List<String> pomation;
    private String joinStatus;  // 가입 상태
    private String additional_info;

}
