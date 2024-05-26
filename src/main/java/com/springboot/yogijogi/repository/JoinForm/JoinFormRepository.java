package com.springboot.yogijogi.repository.JoinForm;

import com.springboot.yogijogi.entity.JoinForm;

import org.springframework.data.jpa.repository.JpaRepository;


public interface JoinFormRepository extends JpaRepository<JoinForm,Long>,JoinFormCustom {

}
