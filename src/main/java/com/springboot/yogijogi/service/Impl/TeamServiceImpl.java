package com.springboot.yogijogi.service.Impl;

import com.springboot.yogijogi.dto.*;
import com.springboot.yogijogi.dto.Team.TeamMoreInfodDto1;
import com.springboot.yogijogi.dto.Team.TeamMoreInfodDto2;
import com.springboot.yogijogi.dto.Team.TeamProfileDto;
import com.springboot.yogijogi.dto.Team.TeamResultDto;
import com.springboot.yogijogi.entity.Team;
import com.springboot.yogijogi.entity.User;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.repository.Team.TeamRepository;
import com.springboot.yogijogi.repository.UserRepository;
import com.springboot.yogijogi.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
    private TeamServiceImpl (TeamRepository teamRepository, JwtProvider jwtProvider,UserRepository userRepository){
        this.teamRepository = teamRepository;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    private User findUser(String token){
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = jwtProvider.getUsername(token);
        User user = userRepository.findByPhoneNum(info);

        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료 info: {} " ,info);
        return user;
    }

    private boolean updateUserRole(String token, String newRole){
        User findUser = findUser(token);
        if(findUser == null){
            logger.info("[updateUserRole] 사용자를 찾을 수 없습니다.");
            return false;
        }

        List<String> roles = findUser.getRoles();
        if(!roles.contains(newRole)){
            roles.add(newRole);//새로운 역할 추가
            findUser.setRoles(roles);
            userRepository.save(findUser);
            logger.info("[updateUserRole] 사용자 역할 업데이트 완료: {}" ,newRole);
            return  true;
        }
        return  false;
    }

    @Override
    public TeamResultDto createTeam(HttpServletRequest request, String token, TeamProfileDto teamProfileDto) {
        User user = findUser(token);

        TeamResultDto teamResultDto = new TeamResultDto();
        if(user == null){
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
        User user = findUser(token);

        Team partialTeam = (Team) request.getSession().getAttribute("partialTeam");
        System.out.println(partialTeam);

        TeamResultDto teamResultDto = new TeamResultDto();
        if(user ==null){
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
        User user = findUser(token);

        Team partialTeam = (Team) request.getSession().getAttribute("partialTeam");
        System.out.println(partialTeam);

        TeamResultDto teamResultDto = new TeamResultDto();
        if(user == null){
            setFail(teamResultDto);
        }else{
            if(partialTeam==null){
                setFail(teamResultDto);
            }else{
                partialTeam.setGender(teamMoreInfodDto2.getGender());
                partialTeam.setAge(teamMoreInfodDto2.getAge());


                teamRepository.save(partialTeam);
                setSuccess(teamResultDto);
            }
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
