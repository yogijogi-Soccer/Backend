package com.springboot.yogijogi.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class ConsentDto {

    private boolean age_consent;  // 선수경험

    private boolean service_consent;  // 선수경험

    private boolean personal_info_consent;  // 선수경험

    private boolean receive_nightly_benefits;  // 선수경험

    private boolean advertisement_marketing;  // 선수경험

    private boolean marketing_personal_info;  // 선수경험
}
