package com.springboot.yogijogi.service;

import com.springboot.yogijogi.entity.Member;
import com.springboot.yogijogi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String phoneNum) throws UsernameNotFoundException {
        logger.info("[loadUserByPhoneNum] : {} ", phoneNum);

        TypedQuery<Member> query = em.createQuery(
                "SELECT m FROM Member m LEFT JOIN FETCH m.memberRoles WHERE m.phoneNum = :phoneNum",
                Member.class);
        query.setParameter("phoneNum", phoneNum);

        Member member = query.getSingleResult();

        if (member == null) {
            throw new UsernameNotFoundException("User not found with phone number: " + phoneNum);
        }

        return member;
    }
}
