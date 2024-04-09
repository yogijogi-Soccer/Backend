package com.springboot.yogijogi.service.Impl;

import com.springboot.yogijogi.dto.*;
import com.springboot.yogijogi.dto.Team.*;
import com.springboot.yogijogi.entity.Member;
import com.springboot.yogijogi.entity.Team;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.repository.Team.TeamRepository;
import com.springboot.yogijogi.repository.MemberRepository;
import com.springboot.yogijogi.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;


@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    private Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
    private TeamServiceImpl (TeamRepository teamRepository, JwtProvider jwtProvider, MemberRepository memberRepository){
        this.teamRepository = teamRepository;
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    private Member findUser(String token){
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = jwtProvider.getUsername(token);
        Member member = memberRepository.findByPhoneNum(info);

        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료 info: {} " ,info);
        return member;
    }

    private boolean updateUserRole(String token, String newRole){
        Member findMember = findUser(token);
        if(findMember == null){
            logger.info("[updateUserRole] 사용자를 찾을 수 없습니다.");
            return false;
        }

        List<String> roles = findMember.getRoles();
        if(!roles.contains(newRole)){
            roles.add(newRole);//새로운 역할 추가
            findMember.setRoles(roles);
            memberRepository.save(findMember);
            logger.info("[updateUserRole] 사용자 역할 업데이트 완료: {}" ,newRole);
            return  true;
        }
        return  false;
    }

    @Override
    public TeamResultDto createTeam(HttpServletRequest request, String token, TeamProfileDto teamProfileDto) {
        Member member = findUser(token);

        TeamResultDto teamResultDto = new TeamResultDto();
        if(member == null){
            setFail(teamResultDto);
        }else{
        updateUserRole(token,"Role_Manager");

        Team team = new Team();
        team.setTeam_name(teamProfileDto.getTeam_name());
        team.setTeam_introduce(teamProfileDto.getTeam_introduce());
        team.setTeam_image(teamProfileDto.getTeam_image());

        request.getSession().setAttribute("partialTeam",team);
        setSuccess(teamResultDto);
        }
        return teamResultDto;
    }

    @Override
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
    public TeamInviteCodeDto JoinInviteCode(String request, String invite_code) {
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
