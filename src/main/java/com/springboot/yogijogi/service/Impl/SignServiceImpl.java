package com.springboot.yogijogi.service.Impl;

import com.springboot.yogijogi.dto.*;
import com.springboot.yogijogi.entity.User;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.redis.SmsCertification;
import com.springboot.yogijogi.repository.UserRepository;
import com.springboot.yogijogi.service.SignService;
import com.springboot.yogijogi.service.SmsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.List;

@Service
public class SignServiceImpl implements SignService {


    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;
    private SmsCertificationDto smsCertificationDto;





    public SignServiceImpl(UserRepository userRepository,
                           JwtProvider jwtProvider,
                           PasswordEncoder passwordEncoder,
                           SmsService smsService
                           ){
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.smsService = smsService;
    }



    @Override
    public SignUpResultDto SignUpVerification(String name, String gender, Long birth_date, String phone_num, boolean certification_num,HttpServletRequest request) {

        String certificationNum = smsService.sendSMS(phone_num);

        // 인증번호를 세션에 저장
        request.getSession().setAttribute("phone_num", phone_num);
        request.getSession().setAttribute("certification_num", certificationNum);

        SignUpResultDto signUpResultDto = new SignUpResultDto();

        // SMS 인증번호 검증
        SmsCertificationDto smsCertificationDto = new SmsCertificationDto();
        smsCertificationDto.setPhone_num(phone_num);
        smsCertificationDto.setCertification_num(certificationNum);
        if (smsService.verifySms(smsCertificationDto)) {
            // 인증 성공 시 RDBMS에 전화번호 저장
            User user = User.builder()
                    .name(name)
                    .gender(gender)
                    .birth_date(birth_date)
                    .phoneNum(phone_num)
                    .certification_num(true)
                    .build();
            request.getSession().setAttribute("partialUser", user);

            setSuccess(signUpResultDto);
        } else {
            setFail(signUpResultDto);
        }
        return signUpResultDto;
    }


    @Override
    public SignUpResultDto SignUpEmailPassword(String email,String password, HttpServletRequest request ) {


        User partialUser = (User) request.getSession().getAttribute("partialUser");
        System.out.println(partialUser);

        if(partialUser != null){
            partialUser.setEmail(email);
            partialUser.setPassword(passwordEncoder.encode(password));

            SignUpResultDto signUpResultDto = new SignUpResultDto();
            if(!partialUser.getEmail().isEmpty()){
                setSuccess(signUpResultDto);
            }else{
                setFail(signUpResultDto);
            }
             return  signUpResultDto;
        }else{
            SignUpResultDto signUpResultDto = new SignUpResultDto();
            setFail(signUpResultDto);
            return signUpResultDto;

        }

    }

    @Override
    public SignUpResultDto SignUpAdditionalInfo(boolean has_experience, List<String> position, HttpServletRequest request) {
        User partialUser = (User) request.getSession().getAttribute("partialUser");
        System.out.println(partialUser);

        if(partialUser != null){
            partialUser.setHas_experience(has_experience);
            partialUser.setPosition(position);
            User savedUser = userRepository.save(partialUser);

            SignUpResultDto signUpResultDto = new SignUpResultDto();
            if(!savedUser.getEmail().isEmpty()){
                setSuccess(signUpResultDto);
            }else{
                setFail(signUpResultDto);
            }
            return  signUpResultDto;
        }else{
            SignUpResultDto signUpResultDto = new SignUpResultDto();
            setFail(signUpResultDto);
            return signUpResultDto;

        }
    }

    @Override
    public SignUpResultDto SignUpAdditionalInfo2(List<String> available_days, String available_time_start,String  available_time_end, HttpServletRequest request) {
        User partialUser = (User) request.getSession().getAttribute("partialUser");
        System.out.println(partialUser);

        if(partialUser != null){

            partialUser.setAvailable_days(available_days);
            partialUser.setAvailable_time_start(available_time_start);
            partialUser.setAvailable_time_end(available_time_start);


            User savedUser = userRepository.save(partialUser);

            SignUpResultDto signUpResultDto = new SignUpResultDto();
            if(!savedUser.getEmail().isEmpty()){
                setSuccess(signUpResultDto);
            }else{
                setFail(signUpResultDto);
            }
            return  signUpResultDto;
        }else{
            SignUpResultDto signUpResultDto = new SignUpResultDto();
            setFail(signUpResultDto);
            return signUpResultDto;
        }
    }

    @Override
    public SignUpResultDto SignUpAgreement(Agreement agreement, HttpServletRequest request) {
        User partialUser = (User) request.getSession().getAttribute("partialUser");

        System.out.println(partialUser);

        if(partialUser != null){

            partialUser.setAll_agreement(agreement.isAll_agreement());
            partialUser.setAll_agreement(agreement.isAge_14_older());
            partialUser.setAll_agreement(agreement.isTerms_of_Service());
            partialUser.setAll_agreement(agreement.isUse_of_personal_information());
            partialUser.setAll_agreement(agreement.isReceive_nightly_benefits());
            partialUser.setAll_agreement(agreement.isPromotion_marketing_use());
            partialUser.setAll_agreement(agreement.isMarketing_personal_information());


            User savedUser = userRepository.save(partialUser);

            SignUpResultDto signUpResultDto = new SignUpResultDto();
            if(!savedUser.getEmail().isEmpty()){
                setSuccess(signUpResultDto);
            }else{
                setFail(signUpResultDto);
            }
            return  signUpResultDto;
        }else{
            SignUpResultDto signUpResultDto = new SignUpResultDto();
            setFail(signUpResultDto);
            return signUpResultDto;
        }

    }


    @Override
    public SignInResultDto SignIn(String phoneNum, String password) {
      User user = userRepository.getByPhoneNum(phoneNum);
      if(!passwordEncoder.matches(password,user.getPassword())){
          throw new RuntimeException();
      }
      SignInResultDto signInResultDto = SignInResultDto.builder()
              .token(jwtProvider.createToken(String.valueOf(user.getPhoneNum())
                      ,user.getRoles()))
              .build();
    setSuccess(signInResultDto);
    return signInResultDto;
    }

    @Override
    public String updatePassword(String phone_num,String password, HttpServletRequest request) {
       String certification_num =  smsService.sendSMS(phone_num);

       request.getSession().setAttribute("phone_num",phone_num);
       request.getSession().setAttribute("certification",certification_num);

       SmsCertificationDto smsCertificationDto2 = new SmsCertificationDto();

       smsCertificationDto2.setPhone_num(phone_num);
       smsCertificationDto2.setCertification_num(certification_num);

        if(smsService.verifySms(smsCertificationDto2)){
            User user = userRepository.findByPhoneNum(smsCertificationDto2.getPhone_num());
            if(user != null){
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return "패스워드가 변경되었습니다.";
            }else{
                return "회원 정보가 없습니다. 회원가입 하세요.";
            }
        }else{

            return "인증시실패";
        }

    }

    private void setSuccess(SignUpResultDto signUpResultDto){
        signUpResultDto.setSuccess(true);
        signUpResultDto.setCode(CommonResponse.SUCCESS.getCode());

        signUpResultDto.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFail(SignUpResultDto signUpResultDto){
        signUpResultDto.setSuccess(true);
        signUpResultDto.setCode(CommonResponse.Fail.getCode());

        signUpResultDto.setMsg(CommonResponse.Fail.getMsg());
    }


}
