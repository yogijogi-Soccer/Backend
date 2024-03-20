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
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    @Column(nullable = false)
    private String team_name;

    @Column(nullable = false)
    private String team_introduce;

    @Column(nullable = true)
    private String team_image;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String town;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private List<String> activity_days;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private List<String> activity_time;

    @Column(nullable = false)
    private String dues = "0";

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String age;

    @Column(nullable = true)
    private String invite_code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User manager; // 팀의 관리자 (감독 또는 총무)






}
