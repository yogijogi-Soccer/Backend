package com.springboot.yogijogi.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String joinStatus = "대기중";  // 가입 상태

    @Column(nullable = false)
    private String Additional_info; // 추가 전달 사항

    @Column(nullable = false)
    private boolean  checked  = false; // 추가 전달 사항


    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "member_uid")
    private Member member;
}
