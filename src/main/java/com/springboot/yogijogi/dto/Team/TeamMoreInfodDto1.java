package com.springboot.yogijogi.dto.Team;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMoreInfodDto1 {
    private String region;

    private String town;

    private List<String> activity_days;

    private List<String> activity_time;

    private String dues;

}
