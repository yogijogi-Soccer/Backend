package com.springboot.yogijogi.service;

import com.springboot.yogijogi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNum) throws UsernameNotFoundException{
        logger.info("[loadUserByPhone_num] : {} " , phoneNum);
        return userRepository.getByPhoneNum(phoneNum);

    }


}
