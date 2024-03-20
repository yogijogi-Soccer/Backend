package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.SignUpIn.SmsCertificationDto;

import java.util.HashMap;

public interface SmsService {
    HashMap<String, String> makeprams(String to, String randomNum);
    String createRandomNumber();
    String sendSMS(String phoneNum);
    boolean verifySms(SmsCertificationDto smsCertificationDto);

}
