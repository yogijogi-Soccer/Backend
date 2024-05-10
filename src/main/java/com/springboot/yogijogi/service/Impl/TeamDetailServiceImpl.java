package com.springboot.yogijogi.service.Impl;

import com.springboot.yogijogi.dto.CommonResponse;
import com.springboot.yogijogi.dto.Team.TeamPlayScheduleDto;
import com.springboot.yogijogi.dto.Team.TeamResultDto;
import com.springboot.yogijogi.dto.MemberDto;
import com.springboot.yogijogi.entity.Member;
import com.springboot.yogijogi.entity.Team;
import com.springboot.yogijogi.entity.TeamDetail;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.repository.MemberRepository;
import com.springboot.yogijogi.repository.TeamDetailRepository;
import com.springboot.yogijogi.service.TeamDetailService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamDetailServiceImpl implements TeamDetailService {

    private final TeamDetailRepository teamDetailRepository;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private Logger logger = LoggerFactory.getLogger(TeamDetailServiceImpl.class);

    public Member findUser(String token){
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = jwtProvider.getUsername(token);
        Member member = memberRepository.findByPhoneNum(info);


        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료 info: {} " ,info);
        return member;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Transactional
    public TeamResultDto TeamPlayRegistration(HttpServletRequest request, String token, TeamPlayScheduleDto teamPlayScheduleDto) {
        TeamResultDto teamResultDto = new TeamResultDto();
        Member member = findUser(token);
        // LazyInitializationException 방지를 위해 memberRoles를 초기화합니다.
        Hibernate.initialize(member.getMemberRoles());
        Team userTeam = member.getTeam();

        if(userTeam != null){
        TeamPlayScheduleDto saveTeamPlayScheduleDto = new TeamPlayScheduleDto();
        saveTeamPlayScheduleDto.setAddress(teamPlayScheduleDto.getAddress());
        saveTeamPlayScheduleDto.setAddressDetail(teamPlayScheduleDto.getAddressDetail());
        saveTeamPlayScheduleDto.setMonth(teamPlayScheduleDto.getMonth());
        saveTeamPlayScheduleDto.setDay(teamPlayScheduleDto.getDay());
        saveTeamPlayScheduleDto.setHour(teamPlayScheduleDto.getHour());
        saveTeamPlayScheduleDto.setMinute(teamPlayScheduleDto.getMinute());
        saveTeamPlayScheduleDto.setOpposingTeam(teamPlayScheduleDto.getOpposingTeam());
        saveTeamPlayScheduleDto.setTactics(teamPlayScheduleDto.getTactics());
        saveTeamPlayScheduleDto.setTodayPositions(teamPlayScheduleDto.getTodayPositions());

        TeamDetail teamDetail = new TeamDetail();
        teamDetail.setAddress(saveTeamPlayScheduleDto.getAddress());
        teamDetail.setAddressDetail(saveTeamPlayScheduleDto.getAddressDetail());
        teamDetail.setMonth(saveTeamPlayScheduleDto.getMonth());
        teamDetail.setDay(saveTeamPlayScheduleDto.getDay());
        teamDetail.setHour(saveTeamPlayScheduleDto.getHour());
        teamDetail.setMinute(saveTeamPlayScheduleDto.getMinute());
        teamDetail.setOpposingTeam(saveTeamPlayScheduleDto.getOpposingTeam());
        teamDetail.setTactics(saveTeamPlayScheduleDto.getTactics());
        teamDetail.setTodayPositions(saveTeamPlayScheduleDto.getTodayPositions());
        teamDetail.setTeam(userTeam);

        teamDetailRepository.save(teamDetail);
        setSuccess(teamResultDto);
        }else{
            throw new IllegalArgumentException("관리자 역할이 아닙니다.");
        }

        return teamResultDto;

    }

    private void setSuccess(TeamResultDto teamResultDto){
        teamResultDto.setSuccess(true);
        teamResultDto.setCode(CommonResponse.SUCCESS.getCode());

        teamResultDto.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFail(TeamResultDto teamResultDto){
        teamResultDto.setSuccess(true);
        teamResultDto.setCode(CommonResponse.Fail.getCode());

        teamResultDto.setMsg(CommonResponse.Fail.getMsg());
    }
}
