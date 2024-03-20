package com.springboot.yogijogi.dto.SignUpIn;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class SignUpEssentialDto {

    private String email;

    private String password;

    private String gender;

    private String name;

    private Long birth_date;  //생년월일

    private String phoneNum;

    private Long certification_num;   // 인증번호

    private boolean has_experience;  // 선수경험

    private List<String> position;   //포지션

    private List<String> available_days ;   //가능 요일

    private String available_time_start;  // 시작 시간

    private String available_time_end;  // 종료 시간
}
