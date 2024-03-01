package com.springboot.yogijogi.controller;


import com.springboot.yogijogi.dto.SignInResultDto;

import com.springboot.yogijogi.dto.SignUpEssentialDto;
import com.springboot.yogijogi.dto.SignUpResultDto;
import com.springboot.yogijogi.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sign-api")
public class SignController {

    private Logger logger = LoggerFactory.getLogger(SignController.class);

    private SignService signService;

    @Autowired
    public SignController(SignService signService){
        this.signService = signService;
    }

    @PostMapping("/sign-up/sign-up-verification")
    public SignUpResultDto SignUpVerification(@RequestParam String name, String gender,Long birth_date,String phone_num,Long certification_num, HttpServletRequest request){
        SignUpEssentialDto signUpEssentialDto = new SignUpEssentialDto();
        logger.info("[signUp] 회원가입을 수행합니다. id : {}, password : ****, name : {}, role : {}", signUpEssentialDto.getPhoneNum(),
                signUpEssentialDto.getPassword());
        SignUpResultDto signUpResultDto = signService.SignUpVerification(name,gender,birth_date,phone_num,certification_num,request);

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
