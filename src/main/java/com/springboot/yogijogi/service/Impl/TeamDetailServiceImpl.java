package com.springboot.yogijogi.service.Impl;

import com.springboot.yogijogi.dto.CommonResponse;

import com.springboot.yogijogi.dto.Team.*;
import com.springboot.yogijogi.entity.*;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.repository.JoinForm.JoinFormRepository;
import com.springboot.yogijogi.repository.MemberRepository;
import com.springboot.yogijogi.repository.MemberRoleRepository;
import com.springboot.yogijogi.repository.PomationRepository;
import com.springboot.yogijogi.repository.Team.TeamRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamDetailServiceImpl implements TeamDetailService {

    private final TeamDetailRepository teamDetailRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final PomationRepository pomationRepository;
    private final JwtProvider jwtProvider;
    private final JoinFormRepository joinFormRepository;
    private Logger logger = LoggerFactory.getLogger(TeamDetailServiceImpl.class);
    @Transactional
    public Member findUser(String token) {
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = jwtProvider.getUsername(token);
        Member member = memberRepository.findByPhoneNum(info);

        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료 info: {}", info);
        return member;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Transactional
    public TeamResultDto TeamPlayRegistration(HttpServletRequest request, String token, TeamPlayScheduleDto teamPlayScheduleDto, String formation_name) {
        TeamResultDto teamResultDto = new TeamResultDto();
        Member member = findUser(token);
        // LazyInitializationException 방지를 위해 memberRoles를 초기화합니다.
        Hibernate.initialize(member.getMemberRoles());
        Team userTeam = member.getTeam();
        Pomation pomation = pomationRepository.findByFormationName(formation_name);
        if (userTeam != null) {
            TeamPlayScheduleDto saveTeamPlayScheduleDto = new TeamPlayScheduleDto();
            saveTeamPlayScheduleDto.setAddress(teamPlayScheduleDto.getAddress());
            saveTeamPlayScheduleDto.setAddressDetail(teamPlayScheduleDto.getAddressDetail());
            saveTeamPlayScheduleDto.setMonth(teamPlayScheduleDto.getMonth());
            saveTeamPlayScheduleDto.setDay(teamPlayScheduleDto.getDay());
            saveTeamPlayScheduleDto.setHour(teamPlayScheduleDto.getHour());
            saveTeamPlayScheduleDto.setMinute(teamPlayScheduleDto.getMinute());
            saveTeamPlayScheduleDto.setOpposingTeam(teamPlayScheduleDto.getOpposingTeam());
            saveTeamPlayScheduleDto.setTactics(teamPlayScheduleDto.getTactics());
            saveTeamPlayScheduleDto.setFormation_player(teamPlayScheduleDto.getFormation_player());

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
            teamDetail.setFormation_player(saveTeamPlayScheduleDto.getFormation_player());

            teamDetail.setPomation(pomation);
            teamDetail.setTeam(userTeam);

            teamDetailRepository.save(teamDetail);
            setSuccess(teamResultDto);
        } else {
            throw new IllegalArgumentException("관리자 역할이 아닙니다.");
        }
        return teamResultDto;
    }

    @Override
    public TeamPlayScheduleDto TeamPlaySchedule(HttpServletRequest request, String token, Long id) {
        TeamPlayScheduleDto scheduleDto = new TeamPlayScheduleDto();
        Member member = findUser(token);
        TeamDetail teamDetail = teamDetailRepository.findByDetailTeamId(id);

        if (member.getTeam() != null) {
            scheduleDto.setAddress(teamDetail.getAddress());
            scheduleDto.setAddressDetail(teamDetail.getAddressDetail());
            scheduleDto.setOpposingTeam(teamDetail.getOpposingTeam());
            scheduleDto.setMonth(teamDetail.getMonth());
            scheduleDto.setDay(teamDetail.getDay());
            scheduleDto.setHour(teamDetail.getHour());
            scheduleDto.setMinute(teamDetail.getMinute());
            scheduleDto.setTactics(teamDetail.getTactics());
            scheduleDto.setFormation_player(teamDetail.getFormation_player());

            return scheduleDto;
        } else {
            throw new IllegalArgumentException("가입된 팀이 아닙니다.");
        }
    }



    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Transactional
    public List<TeamDetailJoinDto> TeamPlayJoinFormSelect(HttpServletRequest request, String token, Long teamId) {
        Member adminMember = findUser(token);

        if (adminMember == null) {
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }

        List<JoinForm> joinForms = joinFormRepository.findPendingJoinFormsByTeam_TeamId(teamId);
        if (joinForms == null || joinForms.isEmpty() ||
                joinForms.stream().noneMatch(joinForm -> "대기중".equals(joinForm.getJoinStatus()))) {
            throw new IllegalArgumentException("가입 대기자가 없습니다.");
        }

        return joinForms.stream().filter(joinForm -> "대기중".equals(joinForm.getJoinStatus())).map(joinForm -> {
            TeamDetailJoinDto teamDetailJoinDto = new TeamDetailJoinDto();
            teamDetailJoinDto.setName(joinForm.getMember().getName());
            teamDetailJoinDto.setJoinStatus(joinForm.getJoinStatus());
            teamDetailJoinDto.setPomation(joinForm.getMember().getPosition());
            teamDetailJoinDto.setAdditional_info(joinForm.getAdditional_info());
            return teamDetailJoinDto;
        }).collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public TeamResultDto JoinFormApprove(HttpServletRequest request, TeamDetailJoinApproveDto teamDetailJoinApproveDto) {
        Member adminMember = findUser(request.getHeader("X-AUTH-TOKEN"));
        TeamResultDto teamResultDto = new TeamResultDto();
        if (adminMember != null) {
            Optional<JoinForm> joinFormOptional = joinFormRepository.findById(teamDetailJoinApproveDto.getId());
            if (joinFormOptional.isPresent()) {
                JoinForm joinForm = joinFormOptional.get();
                joinForm.setJoinStatus(teamDetailJoinApproveDto.getJoinStatus());
                joinFormRepository.save(joinForm);

                if (teamDetailJoinApproveDto.getJoinStatus().equals("승인")) {
                    Member member = joinForm.getMember();
                    Team team = joinForm.getTeam();

                    // 사용자의 팀 소속을 변경
                    member.setTeam(team);
                    // 사용자 정보 저장
                    member.getJoinTeam().add(team.getTeam_name()); // 가입한 팀 목록에 추가
                    saveMemberRole(member, team, "Role_User"); // 역할을 "Role_User"로 추가
                    memberRepository.save(member);
                }
                setSuccess(teamResultDto);
            } else {
                throw new IllegalArgumentException("가입 폼을 찾을 수 없습니다.");
            }
        } else {
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }
        return teamResultDto;
    }

    @Override
    public TeamPomatinDto TeamPomationSelectTest(HttpServletRequest request, String token, String position_name) {
        Pomation positionDetail = pomationRepository.findByFormationName(position_name);
        TeamPomatinDto teamPomatinDto = new TeamPomatinDto();
        teamPomatinDto.setPositionName(positionDetail.getFormationName());
        teamPomatinDto.setPosition_detail(positionDetail.getFormation_detail());
        return teamPomatinDto;
    }

    private void setSuccess(TeamResultDto teamResultDto) {
        teamResultDto.setSuccess(true);
        teamResultDto.setCode(CommonResponse.SUCCESS.getCode());
        teamResultDto.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFail(TeamResultDto teamResultDto) {
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
        memberRoleRepository.save(memberRole);
    }

}


