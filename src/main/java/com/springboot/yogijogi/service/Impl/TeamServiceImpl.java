package com.springboot.yogijogi.service.Impl;

import com.springboot.yogijogi.dto.*;
import com.springboot.yogijogi.dto.Team.*;
import com.springboot.yogijogi.entity.Member;

import com.springboot.yogijogi.entity.MemberRole;
import com.springboot.yogijogi.entity.Team;
import com.springboot.yogijogi.jwt.JwtProvider;

import com.springboot.yogijogi.repository.Team.TeamRepository;
import com.springboot.yogijogi.repository.MemberRepository;
import com.springboot.yogijogi.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    private Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);


    private Member findUser(String token){
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = jwtProvider.getUsername(token);
        Member member = memberRepository.findByPhoneNum(info);

        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료 info: {} " ,info);
        return member;
    }


    @Override
    @Transactional
    public TeamResultDto createTeam(HttpServletRequest request, String token, TeamProfileDto teamProfileDto) {
        Member member = findUser(token);

        TeamResultDto teamResultDto = new TeamResultDto();
        if(member == null){
            setFail(teamResultDto);
        }else{

            Team team = new Team();
            team.setTeam_name(teamProfileDto.getTeam_name());
            team.setTeam_introduce(teamProfileDto.getTeam_introduce());
            team.setTeam_image(teamProfileDto.getTeam_image());

            saveMemberRole(member,team,"Role_Manager");


            request.getSession().setAttribute("partialTeam",team);

            setSuccess(teamResultDto);
        }
        return teamResultDto;
    }

    @Override
    @Transactional
    public TeamResultDto TeamMoreInfo1(HttpServletRequest request, String token, TeamMoreInfodDto1 teamMoreInfodDto1) {
        Member member = findUser(token);

        Team partialTeam = (Team) request.getSession().getAttribute("partialTeam");
        System.out.println(partialTeam);

        TeamResultDto teamResultDto = new TeamResultDto();
        if(member ==null){
            setFail(teamResultDto);
        }else{
            if(partialTeam==null){
                setFail(teamResultDto);
            }else{

                partialTeam.setRegion(teamMoreInfodDto1.getRegion());
                partialTeam.setTown(teamMoreInfodDto1.getTown());
                partialTeam.setActivity_days(teamMoreInfodDto1.getActivity_days());
                partialTeam.setActivity_time(teamMoreInfodDto1.getActivity_time());
                partialTeam.setDues(teamMoreInfodDto1.getDues());

                request.getSession().setAttribute("partialTeam",partialTeam);
                setSuccess(teamResultDto);
            }
        }

        return teamResultDto;
    }

    @Override
    @Transactional
    public TeamResultDto TeamMoreInfo2(HttpServletRequest request, String token, TeamMoreInfodDto2 teamMoreInfodDto2) {
        Member member = findUser(token);

        Team partialTeam = (Team) request.getSession().getAttribute("partialTeam");
        System.out.println(partialTeam);
        String invite_code = make_InviteCode();

        TeamResultDto teamResultDto = new TeamResultDto();
        if(member == null){
            setFail(teamResultDto);
        }else{
            if(partialTeam==null){
                setFail(teamResultDto);
            }else{
                partialTeam.setGender(teamMoreInfodDto2.getGender());
                partialTeam.setAge(teamMoreInfodDto2.getAge());
                partialTeam.setInviteCode(invite_code);

                List<String>createTeam = member.getCreateTeam();
                createTeam.add(partialTeam.getTeam_name());
                member.setTeam(partialTeam);

                List<String>joinTeams = member.getJoinTeam();
                joinTeams.add(partialTeam.getTeam_name());


                teamRepository.save(partialTeam);
                setSuccess(teamResultDto);
            }
        }

        return teamResultDto;
    }

    private String make_InviteCode(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;
        Random random = new Random();

        String randomNum = random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return randomNum;
    }



    @Override
    @Transactional(readOnly = true)
    public TeamInviteCodeDto SelectTeamByInviteCode(HttpServletRequest request,String token,String invite_code) {
        // 초대 코드에 해당하는 팀을 데이터베이스에서 검색
        Team team = teamRepository.findByInviteCode(invite_code);

        if (team != null) { // 팀이 존재하는 경우
            // 초대 코드가 일치하는 경우
            if (invite_code.equals(team.getInviteCode())) {

                TeamInviteCodeDto teamInviteCodeDto = new TeamInviteCodeDto();
                teamInviteCodeDto.setInvite_code(invite_code);
                teamInviteCodeDto.setTeam_name(team.getTeam_name());
                teamInviteCodeDto.setTeam_image(team.getTeam_image());
                logger.info("[teamInviteCodeDto] : {} ", teamInviteCodeDto);

                request.getSession().setAttribute("partialTeam",team);
                return teamInviteCodeDto;
            } else {
                // 초대 코드가 일치하지 않는 경우 예외를 던집니다.
                throw new IllegalArgumentException("팀이 일치하지 않습니다.");
            }
        } else {
            // 팀이 존재하지 않는 경우 예외를 던집니다.
            throw new IllegalArgumentException("팀이 존재하지 않습니다.");
        }
    }

    @Override
    @Transactional
    public TeamResultDto JoinUpByInviteCode(HttpServletRequest request, String token) {
        Team partialTeam = (Team) request.getSession().getAttribute("partialTeam");
        TeamResultDto teamResultDto = new TeamResultDto();

        Member member = findUser(token);


        if (partialTeam != null) { // 세션에 팀 객체가 있는 경우
            // 사용자의 팀 소속을 변경
            member.setTeam(partialTeam);
            // 사용자 정보 저장
            member.getJoinTeam().add(partialTeam.getTeam_name()); // 가입한 팀 목록에 추가

            saveMemberRole(member, partialTeam, "Role_User"); // 역할을 "Role_User"로 추가
            memberRepository.save(member);
            setSuccess(teamResultDto);
        } else {
            // 세션에 팀 객체가 없는 경우 예외를 던집니다.
            throw new IllegalArgumentException("초대코드가 일치하지 않습니다.");
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

    private void saveMemberRole(Member member, Team team, String role) {
        MemberRole memberRole = new MemberRole();
        memberRole.setMember(member);
        memberRole.setTeam(team);
        memberRole.setRole(role);
        member.getMemberRoles().add(memberRole);
    }


}