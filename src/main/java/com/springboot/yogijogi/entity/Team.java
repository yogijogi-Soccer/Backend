package com.springboot.yogijogi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.multipart.MultipartFile;

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

    @Transient // 데이터베이스에 저장하지 않음
    private MultipartFile team_image; // 이미지 파일을 업로드하기 위한 필드

    @Column(nullable = true)
    private String team_imageUrl; // 이미지 파일의 경로를 저장하는 필드
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



    @BatchSize(size=1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_uid") // 매니저를 참조하는 외래 키
    private Member member; // 매니저 정보 추가

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @BatchSize(size=10) // 예시로 설정한 값. 실제 적용에 맞게 변경
    private List<MemberRole> memberRoles;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @BatchSize(size=10) // 예시로 설정한 값. 실제 적용에 맞게 변경
    private List<JoinForm> joinForms;





}
