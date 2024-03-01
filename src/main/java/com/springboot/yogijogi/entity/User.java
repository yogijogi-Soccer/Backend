package com.springboot.yogijogi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalTime;
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
public class User implements UserDetails {
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
    private Long certification_num;   // 인증번호

    @Column(nullable = true)
    private boolean has_experience;  // 선수경험

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = true)
    private List<String> position;   //포지션

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = true)
    private List<String> available_days ;   //가능 요일

    @Column(nullable = true)
    private LocalTime available_time_start;  // 시작 시간

    @Column(nullable = true)
    private LocalTime  available_time_end;  // 종료 시간




    //jpa에서 컬렉션 타입을 사용할때 쓰는 어노테이션
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
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


}
