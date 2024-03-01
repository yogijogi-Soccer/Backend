package com.springboot.yogijogi.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean age_consent;  // 선수경험

    @Column(nullable = false)
    private boolean service_consent;  // 선수경험

    @Column(nullable = false)
    private boolean personal_info_consent;  // 선수경험

    @Column(nullable = true)
    private boolean receive_nightly_benefits;  // 선수경험

    @Column(nullable = true)
    private boolean advertisement_marketing;  // 선수경험

    @Column(nullable = true)
    private boolean marketing_personal_info;  // 선수경험

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="user_id")
    private User user;
}
