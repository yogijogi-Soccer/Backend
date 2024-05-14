package com.springboot.yogijogi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailTeamId;

    private String address;

    private String addressDetail;

    private int month;

    private int day;

    private int hour;

    private int minute;

    private String  opposingTeam;

    private String tactics;

    private String formationName;

    @OneToOne
    @JoinColumn(name = "pomation_id")
    Pomation pomation;

    @OneToOne
    @JoinColumn(name = "team_id")
    Team team;

}
