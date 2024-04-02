package com.springboot.yogijogi.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.yogijogi.dto.*;
import com.springboot.yogijogi.dto.SignUpIn.Agreement;
import com.springboot.yogijogi.dto.SignUpIn.SignInResultDto;
import com.springboot.yogijogi.dto.SignUpIn.SignUpResultDto;
import com.springboot.yogijogi.dto.SignUpIn.SmsCertificationDto;
import com.springboot.yogijogi.entity.User;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.kakao.KakaoProfile;
import com.springboot.yogijogi.kakao.OauthToken;
import com.springboot.yogijogi.repository.UserRepository;
import com.springboot.yogijogi.service.SignService;
import com.springboot.yogijogi.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class SignServiceImpl implements SignService {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;
    private SmsCertificationDto smsCertificationDto;

    private Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);


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

    //카카오톡 인가코드 받아오기


    @Override
    public OauthToken getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",clientId);
        params.add("redirect_uri",redirectUri);
        params.add("code",code);
        params.add("client_secret",clientSecret);

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest =
                new HttpEntity<>(params,headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String reponseBody = accessTokenResponse.getBody();

        // 객체로부터 JSON 형태의 문자열을 만들어냄 -> 직렬화

        ObjectMapper objectMapper = new ObjectMapper();

        OauthToken oauthToken = null;

        try{
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        }catch(JsonMappingException e){
            e.printStackTrace();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        logger.info("[getAccessToken] accessToken :{} " , oauthToken.getAccess_token());
        System.out.println("KakaoAccessToken : "+oauthToken.getAccess_token());

        return oauthToken;

    }

    @Override
    public String KakaoLogin(String token, HttpServletRequest request) {
     return null;
    }

    // 본인인증 페이지
    @Override
    public SignUpResultDto SignUpVerification(String name, String gender, Long birth_date, String phone_num, boolean certification_num, HttpServletRequest request) {

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
    public KakaoProfile findProfile(String token) {
        RestTemplate rt = new RestTemplate();

        OauthToken oauthToken = null;
        HttpHeaders headers = new HttpHeaders();

        //Bearer: OAuth 2.0 토큰을 사용하는 인증 스킴의 일종
        //"Bearer" 다음에 오는 토큰 값은 서버가 클라이언트를 식별하고 인증.
        headers.add("Authorization","Bearer "+ token);
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String>>kakaoProfileRequest =
                new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        String responseBody = kakaoProfileResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;

        try{
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(kakaoProfile);
        return kakaoProfile;
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
            partialUser.setAvailable_time_end(available_time_end);


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
