package com.springboot.yogijogi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pomation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String formationName;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = true)
    private List<String> position_detail;

}
