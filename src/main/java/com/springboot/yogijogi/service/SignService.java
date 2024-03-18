package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.List;

public interface SignService {
    SignUpResultDto SignUpVerification(String name, String gender,Long birth_date,String phone_num,boolean certification_num,
                                       HttpServletRequest request);
    SignUpResultDto SignUpEmailPassword(String email,String password
                                         , HttpServletRequest request);
    SignUpResultDto SignUpAdditionalInfo(boolean has_experience, List<String> position, HttpServletRequest request);

    SignUpResultDto SignUpAdditionalInfo2(List<String> available_days, String available_time_start, String  available_time_end, HttpServletRequest request);
    public SignUpResultDto SignUpAgreement(Agreement agreement, HttpServletRequest request);

    SignInResultDto SignIn(String phoneNum, String password);
}
