package com.springboot.yogijogi.service.Impl;

import com.springboot.yogijogi.dto.*;
import com.springboot.yogijogi.entity.User;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.repository.UserRepository;
import com.springboot.yogijogi.service.SignService;
import org.hibernate.*;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.FlushModeType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Collections;

@Service
public class SignServiceImpl implements SignService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public SignServiceImpl(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public SignUpResultDto SignUpVerification(String name, String gender,Long birth_date,String phone_num,Long certification_num,
                                              HttpServletRequest request) {
        User user;
            SignUpEssentialDto signUpEssentialDto = new SignUpEssentialDto();
            user = User.builder()
                    .name(name)
                    .gender(gender)
                    .birth_date(birth_date)
                    .phoneNum(phone_num)
                    .certification_num(certification_num)
                    .build();

        request.getSession().setAttribute("partialUser",user);



        SignUpResultDto signUpResultDto = new SignUpResultDto();

        setSuccess(signUpResultDto);
        return signUpResultDto;
    }

    @Override
    public SignUpResultDto SignUpEmailPassword(String email,String password, HttpServletRequest request ) {


        User partialUser = (User) request.getSession().getAttribute("partialUser");
        System.out.println(partialUser);
        SignUpEssentialDto signUpEssentialDto = new SignUpEssentialDto();
        if(partialUser != null){
            partialUser.setEmail(email);
            partialUser.setPassword(passwordEncoder.encode(password));
            User savedUser = userRepository.save(partialUser);

            SignUpResultDto signUpResultDto = new SignUpResultDto();
            if(!savedUser.getEmail().isEmpty()){
                setSuccess(signUpResultDto);
            }else{
                setFail(signUpResultDto);
            }
            request.getSession().removeAttribute("partialUser");
            return  signUpResultDto;
        }else{
            SignUpResultDto signUpResultDto = new SignUpResultDto();
            setFail(signUpResultDto);
            return signUpResultDto;

        }





    }

    //    @Override
//    public SignUpResultDto SignChoiceUp(SignUpChoiceDto signUpChoiceDto, String roles) {
//        User user;
//        if(roles.equalsIgnoreCase("admin")){
//            user = User.builder()
//                    .has_experience(signUpChoiceDto.isHas_experience())
//                    .position(signUpChoiceDto.getPosition())
//                    .available_days(signUpChoiceDto.getAvailable_days())
//                    .available_time_start(signUpChoiceDto.getAvailable_time_start())
//                    .available_time_end(signUpChoiceDto.getAvailable_time_end())
//                    .roles(Collections.singletonList("ROLE_ADMIN"))
//                    .build();
//        }else{
//            user = User.builder()
//                    .has_experience(signUpChoiceDto.isHas_experience())
//                    .position(signUpChoiceDto.getPosition())
//                    .available_days(signUpChoiceDto.getAvailable_days())
//                    .available_time_start(signUpChoiceDto.getAvailable_time_start())
//                    .available_time_end(signUpChoiceDto.getAvailable_time_end())
//                    .roles(Collections.singletonList("ROLE_USER"))
//                    .build();
//
//        }
//        User savedUser = userRepository.save(user);
//
//        SignUpResultDto signUpResultDto = new SignUpResultDto();
//
//        if(!savedUser.getPhoneNum().isEmpty()){
//            setSuccess(signUpResultDto);
//        }else{
//            setFail(signUpResultDto);
//        }
//        return signUpResultDto;
//    }

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
