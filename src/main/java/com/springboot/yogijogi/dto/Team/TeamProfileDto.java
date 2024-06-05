package com.springboot.yogijogi.dto.Team;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@NoArgsConstructor
public class TeamProfileDto {
    private String team_name;
    private String team_introduce;

}
