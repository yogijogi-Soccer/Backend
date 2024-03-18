package com.springboot.yogijogi.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsCertificationDto {
    private String phone_num;
    private String certification_num;
}
