package com.springboot.yogijogi.dto.Team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamPomatinDto {
    private String positionName;

    private List<String> position_detail;
}
