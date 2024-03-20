package com.springboot.yogijogi.dto.Team;

import lombok.*;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TeamResultDto {
    private boolean success;
    private int code;
    private String msg;
}