package com.springboot.yogijogi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;  // id 식별값

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long birth_date;  //생년월일

    @Column(nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    private boolean certification_num;   // 인증번호

    @Column(nullable = true)
    private boolean has_experience;  // 선수경험

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = true)
    private List<String> position;   //포지션

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = true)
    private List<String> available_days ;   //가능 요일

    @Column(nullable = true)
    private String available_time_start;  // 시작 시간

    @Column(nullable = true)
    private String  available_time_end;  // 종료 시간


    @Column(nullable = true)
    boolean all_agreement;

    @Column(nullable = false)
    boolean Age_14_older;

    @Column(nullable = false)
    boolean terms_of_Service;

    @Column(nullable = false)
    boolean use_of_personal_information;

    @Column(nullable = true)
    boolean receive_nightly_benefits;

    @Column(nullable = true)
    boolean promotion_marketing_use;

    @Column(nullable = true)
    boolean marketing_personal_information;

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.memberRoles.stream()
                .map(memberRole -> new SimpleGrantedAuthority(memberRole.getRole()))
                .collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> joinTeam;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> createTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size=1)
    @JoinColumn(name = "team_id") // Member 엔티티에서 Team 엔티티를 참조하는 외래 키
    private Team team;

    //멤버는Eager로 해야된다 그이유는 멤버조회할때 항상 MemberRole이 조회되어야 하기 때문이다.
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberRole> memberRoles;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<JoinForm> joinForms;


}
