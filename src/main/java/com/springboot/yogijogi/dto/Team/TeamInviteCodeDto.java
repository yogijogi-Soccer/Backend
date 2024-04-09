package com.springboot.yogijogi.dto.Team;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamInviteCodeDto {
    private String invite_code;
    private String team_name;
    private String team_image;


}
