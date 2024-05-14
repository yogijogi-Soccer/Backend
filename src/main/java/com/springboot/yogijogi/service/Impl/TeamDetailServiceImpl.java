package com.springboot.yogijogi.service.Impl;

import com.springboot.yogijogi.dto.CommonResponse;
import com.springboot.yogijogi.dto.Team.TeamDetailJoinDto;
import com.springboot.yogijogi.dto.Team.TeamPlayScheduleDto;
import com.springboot.yogijogi.dto.Team.TeamPomatinDto;
import com.springboot.yogijogi.dto.Team.TeamResultDto;
import com.springboot.yogijogi.entity.*;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.repository.MemberRepository;
import com.springboot.yogijogi.repository.PomationRepository;
import com.springboot.yogijogi.repository.Team.TeamRepository;
import com.springboot.yogijogi.repository.TeamDetailRepository;
import com.springboot.yogijogi.service.TeamDetailService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamDetailServiceImpl implements TeamDetailService {

    private final TeamDetailRepository teamDetailRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final PomationRepository pomationRepository;
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
    public TeamResultDto TeamPlayRegistration(HttpServletRequest request, String token, TeamPlayScheduleDto teamPlayScheduleDto,String formation_name) {
        TeamResultDto teamResultDto = new TeamResultDto();
        Member member = findUser(token);
        // LazyInitializationException 방지를 위해 memberRoles를 초기화합니다.
        Hibernate.initialize(member.getMemberRoles());
        Team userTeam = member.getTeam();
        Pomation pomation = pomationRepository.findByFormationName(formation_name);
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



            TeamDetail teamDetail = new TeamDetail();
            teamDetail.setAddress(saveTeamPlayScheduleDto.getAddress());
            teamDetail.setAddressDetail(saveTeamPlayScheduleDto.getAddressDetail());
            teamDetail.setMonth(saveTeamPlayScheduleDto.getMonth());
            teamDetail.setDay(saveTeamPlayScheduleDto.getDay());
            teamDetail.setHour(saveTeamPlayScheduleDto.getHour());
            teamDetail.setMinute(saveTeamPlayScheduleDto.getMinute());
            teamDetail.setOpposingTeam(saveTeamPlayScheduleDto.getOpposingTeam());
            teamDetail.setTactics(saveTeamPlayScheduleDto.getTactics());
            teamDetail.setFormationName(formation_name);


            teamDetail.setPomation(pomation);
            teamDetail.setTeam(userTeam);

            teamDetailRepository.save(teamDetail);
            setSuccess(teamResultDto);

        }else{
            throw new IllegalArgumentException("관리자 역할이 아닙니다.");
        }

        return teamResultDto;

    }

    @Override
    public TeamPlayScheduleDto TeamPlaySchedule(HttpServletRequest request, String token, Long id) {
        TeamPlayScheduleDto scheduleDto = new TeamPlayScheduleDto();
        Member member = findUser(token);
        TeamDetail teamDetail = teamDetailRepository.findByDetailTeamId(id);
        if(member.getTeam() != null){

            scheduleDto.setAddress(teamDetail.getAddress());
            scheduleDto.setAddressDetail(teamDetail.getAddressDetail());
            scheduleDto.setOpposingTeam(teamDetail.getOpposingTeam());
            scheduleDto.setMonth(teamDetail.getMonth());
            scheduleDto.setDay(teamDetail.getDay());
            scheduleDto.setHour(teamDetail.getHour());
            scheduleDto.setMinute(teamDetail.getMinute());
            scheduleDto.setTactics(teamDetail.getTactics());


            return scheduleDto;

        }else{
            throw new IllegalArgumentException("가입된 팀이 아닙니다.");
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Transactional
    public TeamDetailJoinDto TeamPlayJoinFormSelect(HttpServletRequest request, String token, Long id) {
        Member member = findUser(token);
        Team team = teamRepository.findByTeamId(id);
        TeamDetailJoinDto teamDetailJoin= new TeamDetailJoinDto();
        JoinForm joinForm = new JoinForm();
        if (member != null && member.getTeam().equals(team)) {
                teamDetailJoin.setName(joinForm.getMember().getName());
                teamDetailJoin.setChecked(joinForm.getJoinStatus());
                teamDetailJoin.setPosition(joinForm.getMember().getPosition());

            return teamDetailJoin;

        } else {
            throw new IllegalArgumentException("가입한 유저가 없습니다.");
        }
    }

    @Override
    public TeamPomatinDto TeamPomationSelectTest(HttpServletRequest request, String token,String position_name) {

        Pomation positionDetail = pomationRepository.findByFormationName(position_name);
        TeamPomatinDto teamPomatinDto = new TeamPomatinDto();
        teamPomatinDto.setPositionName(positionDetail.getFormationName());
        teamPomatinDto.setPosition_detail(positionDetail.getPosition_detail());
        return teamPomatinDto;
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
