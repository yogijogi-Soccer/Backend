package com.springboot.yogijogi.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.time.LocalTime;
import java.util.List;

@Data
@Getter
@Setter
public class Additional_info {

    private boolean has_experience;  // 선수경험

    private List<String> position;   //포지션

    private List<String> available_days ;   //가능 요일

    private LocalTime available_time_start;  // 시작 시간

    private LocalTime  available_time_end;  // 종료 시간


}
