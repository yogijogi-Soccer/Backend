package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.SmsCertificationDto;

import java.util.HashMap;

public interface SmsService {
    HashMap<String, String> makeprams(String to, String randomNum);
    String createRandomNumber();
    String sendSMS(String phone_num);

//    String verifySms(SmsCertificationDto smsCertificationDto);
//
//    boolean isVerify(SmsCertificationDto smsCertificationDto);

}
