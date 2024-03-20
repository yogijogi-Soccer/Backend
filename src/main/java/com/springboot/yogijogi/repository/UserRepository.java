package com.springboot.yogijogi.repository;

import com.springboot.yogijogi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User getByPhoneNum(String phoneNum);
    User findByPhoneNum(String phoneNum);

}
