package com.springboot.yogijogi.repository;

import com.springboot.yogijogi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Member getByPhoneNum(String phoneNum);
    Member findByPhoneNum(String phoneNum);
    Member findByUid(Long uid);

}
