package com.springboot.yogijogi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

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

    @Column(nullable = false)
    private String play_ground;


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
    private String inviteCode;

    private String level;




    @ManyToOne
    @JoinColumn(name = "member_uid") // 매니저를 참조하는 외래 키
    private Member member; // 매니저 정보 추가

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<MemberRole> memberRoles;

    @OneToMany(mappedBy = "team",fetch = FetchType.LAZY)
    private List<JoinForm> joinForms;



}
