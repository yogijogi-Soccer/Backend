package com.springboot.yogijogi.controller;


import com.springboot.yogijogi.dto.*;

import com.springboot.yogijogi.service.SignService;
import com.springboot.yogijogi.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sign-api")
public class SignController {

    private Logger logger = LoggerFactory.getLogger(SignController.class);

    private final SignService signService;
    private final SmsService smsService;

    @Autowired
    public SignController(SignService signService,SmsService smsService){
        this.signService = signService;
        this.smsService = smsService;
    }

    @PostMapping("/sign-up/sign-up-verification")
    public SignUpResultDto SignUpVerification(@RequestParam String name, String gender,Long birth_date,SmsCertificationDto smsCertificationDto, HttpServletRequest request){
        SignUpEssentialDto signUpEssentialDto = new SignUpEssentialDto();
        logger.info("[signUp] 회원가입을 수행합니다. id : {}, password : ****, name : {}, role : {}", signUpEssentialDto.getPhoneNum(),
                signUpEssentialDto.getPassword());
        SignUpResultDto signUpResultDto = signService.SignUpVerification(name,gender,birth_date,smsCertificationDto,request);

        return signUpResultDto;
    }

    @PostMapping("/sign-up/sign-up-email-password")
    public SignUpResultDto SignUpEmailPassword(@RequestParam  String email,String password,HttpServletRequest request){
        SignUpEssentialDto signUpEssentialDto = new SignUpEssentialDto();
        logger.info("[signUp] 회원가입을 수행합니다. id : {}, password : ****, name : {}", signUpEssentialDto.getPhoneNum(),
                signUpEssentialDto.getPassword());
        SignUpResultDto signUpResultDto = signService.SignUpEmailPassword(email,password,request);

        return signUpResultDto;
    }

    @PostMapping("/sign-up/sign-up-addtional-info")
    public SignUpResultDto SignUpAdditionalInfo(@RequestParam  boolean has_experience, @RequestParam List<String> position, HttpServletRequest request){
        Additional_info additionalInfo = new Additional_info();
        logger.info("[signUp] 회원가입을 수행합니다. has_experience : {}, position : {}", additionalInfo.isHas_experience(),
                additionalInfo.getPosition());
        SignUpResultDto signUpResultDto = signService.SignUpAdditionalInfo(has_experience,position,request);

        return signUpResultDto;
    }

    @PostMapping("/sign-up/sign-up-addtional-info2")
    public SignUpResultDto SignUpAdditionalInfo2(@RequestParam List<String> available_days, String available_time_start,String available_time_end , HttpServletRequest request){
        Additional_info additionalInfo = new Additional_info();
        logger.info("[signUp] 회원가입을 수행합니다. available_days : {}, available_time_start : {}, available_time_end : {}", additionalInfo.getAvailable_days(),additionalInfo.getAvailable_time_start(),additionalInfo.getAvailable_time_end());
        SignUpResultDto signUpResultDto = signService.SignUpAdditionalInfo2(available_days,available_time_start,available_time_end,request);

        return signUpResultDto;
    }


    @PostMapping("/sign-up/sign-up-agreement")
    public SignUpResultDto SignUpAgreement( Agreement agreement, HttpServletRequest request) {
        logger.info("[signUp] 회원가입을 수행합니다. agreement.isAge_14_older() : {}, position : {}",
               agreement.isAge_14_older());
        SignUpResultDto signUpResultDto = signService.SignUpAgreement(agreement,request);

        return signUpResultDto;
    }


//    @PostMapping("/sign-up/sign-up-choice")
//    public SignUpResultDto SignUp(@RequestBody SignUpChoiceDto signUpChoiceDto, String roles){
//        logger.info("[signUp] 추가 회원정보를 입력합니다. id : {}, password : ****, name : {}, role : {}", signUpChoiceDto.getPosition(),
//                signUpChoiceDto.isHas_experience(),roles);
//        SignUpResultDto signUpResultDto = signService.SignChoiceUp(signUpChoiceDto,roles);
//
//        return signUpResultDto;
//    }

    @PostMapping("/sign-in")
    public SignInResultDto SignIn(@RequestParam String phoneNum, String password) {
        logger.info("[sign-in] 로그인을 시도하고 있습니다. id : {}, password : *****", phoneNum);
        SignInResultDto signInResultDto = signService.SignIn(phoneNum, password);
        if(signInResultDto.getCode() == 0){
            logger.info("[sign-in] 정상적으로 로그인이 되었습니다. id: {}, token : {}",phoneNum,signInResultDto.getToken());
            signInResultDto.getToken();
        }
        return signInResultDto;
        }
    @GetMapping(value = "/exception")
    public void exceptionTest()throws  RuntimeException{
        throw new RuntimeException("접근이 금지 되었습니다.");
     }

    @PostMapping("/sign-api/sms")
    public ResponseEntity<String> sendSMS(@RequestParam String phone_num) {
        try {
            String randomNum = smsService.sendSMS(phone_num);
            logger.info("[문자 인증 진행중] phoneNumber: {}, randomNum: {}", phone_num,randomNum);
            return ResponseEntity.ok("문자 전송 완료: 인증번호 " + randomNum);
        } catch (Exception e) {
            logger.error("[문자 인증 실패] phoneNumber: {}, error: {}",phone_num, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("문자 전송 실패");
        }
    }
//    @PostMapping("/sign-api/sms-verify-process")
//    public ResponseEntity<String> verifySms(SmsCertificationDto smsCertificationDto){
//        smsService.verifySms(smsCertificationDto);
//        return ResponseEntity.status(HttpStatus.OK).body("인증완료");
//    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        //responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        logger.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
        }
    }


