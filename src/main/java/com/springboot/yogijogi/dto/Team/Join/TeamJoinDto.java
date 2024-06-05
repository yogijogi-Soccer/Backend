package com.springboot.yogijogi.dto.Team.Join;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamJoinDto {

    private String Additional_info; // 추가 전달 사항

    private boolean checked; // 확인 체크 박스
}
