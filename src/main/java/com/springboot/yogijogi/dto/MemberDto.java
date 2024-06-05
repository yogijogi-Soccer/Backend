package com.springboot.yogijogi.dto;

import com.springboot.yogijogi.entity.MemberRole;
import lombok.*;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long uid;
    private String phone_num;
    private String name;
    private List<String> memberRole;
}
