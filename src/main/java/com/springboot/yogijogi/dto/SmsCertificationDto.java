package com.springboot.yogijogi.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Data
public class SmsCertificationDto {
    private String phone_num;
    private String certification_num;
}
