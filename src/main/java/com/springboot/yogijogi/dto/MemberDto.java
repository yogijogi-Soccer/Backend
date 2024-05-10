package com.springboot.yogijogi.dto;

import com.springboot.yogijogi.entity.MemberRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private String phone_num;
    private MemberRole memberRole;
}
