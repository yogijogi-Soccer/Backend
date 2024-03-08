package com.springboot.yogijogi.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
@Getter
@Setter
@Data

public class Agreement {

    boolean all_agreement;


    boolean Age_14_older;


    boolean terms_of_Service;


    boolean use_of_personal_information;


    boolean receive_nightly_benefits;


    boolean promotion_marketing_use;


    boolean marketing_personal_information;
}
