package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.SignInResultDto;
import com.springboot.yogijogi.dto.SignUpEssentialDto;
import com.springboot.yogijogi.dto.SignUpResultDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface SignService {
    SignUpResultDto SignUpVerification(String name, String gender,Long birth_date,String phone_num,Long certification_num,
                                       HttpServletRequest request);
    SignUpResultDto SignUpEmailPassword(String email,String password
                                         , HttpServletRequest request);


    //    SignUpResultDto SignChoiceUp(SignUpChoiceDto sIgnUpDto, String roles);
    SignInResultDto SignIn(String phoneNum, String password);
}
