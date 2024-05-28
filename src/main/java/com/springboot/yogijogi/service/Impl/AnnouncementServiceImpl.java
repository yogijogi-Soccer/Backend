package com.springboot.yogijogi.service.Impl;

import com.springboot.yogijogi.dto.Announcement.AnnouncementDetailDto;
import com.springboot.yogijogi.dto.Announcement.AnnouncementDto;
import com.springboot.yogijogi.dto.Announcement.AnnouncementSelectDto;
import com.springboot.yogijogi.dto.CommonResponse;
import com.springboot.yogijogi.dto.Team.TeamResultDto;
import com.springboot.yogijogi.entity.Announcement;
import com.springboot.yogijogi.entity.Member;
import com.springboot.yogijogi.entity.Team;
import com.springboot.yogijogi.jwt.JwtProvider;
import com.springboot.yogijogi.repository.Announcement.AnnouncementRepository;
import com.springboot.yogijogi.repository.JoinForm.JoinFormRepository;
import com.springboot.yogijogi.repository.MemberRepository;
import com.springboot.yogijogi.repository.MemberRoleRepository;
import com.springboot.yogijogi.repository.PomationRepository;
import com.springboot.yogijogi.repository.Team.TeamRepository;
import com.springboot.yogijogi.service.AnnouncementService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    // 관리자 권한 확인 메서드
    private boolean isAdmin(HttpServletRequest request) {
        String token = request.getHeader("X-AUTH-TOKEN");
        if (token != null) {
            Member member = memberRepository.findByPhoneNum(jwtProvider.getUsername(token));
            if (member != null) {
                return member.getMemberRoles().stream().anyMatch(role -> role.getRole().equals("ROLE_MANAGER"));
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementSelectDto> selectAnnouncementList(HttpServletRequest request, Long id) {
        Member member = findUser(request.getHeader("X-AUTH-TOKEN"));
        Team team = teamRepository.findByTeamId(id);

        if (member.getTeam().getTeamId().equals(team.getTeamId())) {
            List<Announcement> announcements = announcementRepository.findByTeam(team);

            if (announcements.isEmpty()) {
                throw new EntityNotFoundException("해당 팀의 공지사항을 찾을 수 없습니다.");
            }

            List<AnnouncementSelectDto> announcementDtos = new ArrayList<>();
            for (Announcement announcement : announcements) {
                // 조회수 업데이트
                announcement.setViewCount(announcement.getViewCount());
                announcementRepository.save(announcement);

                AnnouncementSelectDto announcementSelectDto = new AnnouncementSelectDto();
                announcementSelectDto.setId(announcement.getId());
                announcementSelectDto.setTitle(announcement.getTitle());
                announcementSelectDto.setCreate_At(announcement.getCreate_At());
                announcementSelectDto.setViewCount(announcement.getViewCount());

                announcementDtos.add(announcementSelectDto);
            }

            return announcementDtos;
        } else {
            throw new IllegalArgumentException("가입된 팀이 아닙니다.");
        }
    }


    @Override
    public AnnouncementDetailDto selectAnnouncementDetail(HttpServletRequest request, Long teamId, Long id) {
        //팀에 가입된 멤버는 공지사항을 볼 수 있습니다.
        //한 유저는 여러 팀에 가입되어 있다.
        //유저가 여러팀중 한팀의 공지사항을 볼 수 있다.
        Member member = findUser(request.getHeader("X-AUTH-TOKEN"));
        Team team = teamRepository.findByTeamId(teamId);

        if(member.getTeam().getTeamId().equals(team.getTeamId())){
             Announcement announcement = announcementRepository.findById(id).orElseThrow(()
                    -> new EntityNotFoundException("해당 ID의 공지사항을 찾을 수 없습니다."));

            AnnouncementDetailDto announcementDetailDto = new AnnouncementDetailDto();
            announcementDetailDto.setTitle(announcement.getTitle());
            announcementDetailDto.setContent(announcement.getContent());
            announcementDetailDto.setViewCount(announcement.getViewCount()+1);
            announcementDetailDto.setCreate_At(announcement.getCreate_At());

            return announcementDetailDto;
        }else{
            throw new IllegalArgumentException("가입된 팀이 아닙니다.");
        }

    }

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')") // 관리자만 게시물 생성 가능하도록 설정
    public AnnouncementDto saveAnnouncement(HttpServletRequest request, AnnouncementDto announcementDto) {
        if (!isAdmin(request)) { // 관리자 권한 확인
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }

        Member member = findUser(request.getHeader("X-AUTH-TOKEN"));
        Team team = teamRepository.findByTeamId(member.getTeam().getTeamId());
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementDto.getTitle());
        announcement.setContent(announcementDto.getContent());
        announcement.setMember(member);
        announcement.setTeam(team);
        announcement.setCreate_At(LocalDateTime.now());
        announcement.setUpdate_At(LocalDateTime.now());

        announcementRepository.save(announcement);

        return announcementDto;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')") // 관리자만 게시물 수정 가능하도록 설정
    public AnnouncementDto updateAnnouncement(HttpServletRequest request, Long id, AnnouncementDto announcementDto) {
        if (!isAdmin(request)) { // 관리자 권한 확인
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }

        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 공지사항을 찾을 수 없습니다."));

        announcement.setTitle(announcementDto.getTitle());
        announcement.setContent(announcementDto.getContent());
        announcement.setUpdate_At(LocalDateTime.now());
        announcementRepository.save(announcement);

        return announcementDto;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')") // 관리자만 게시물 삭제 가능하도록 설정
    public void deleteAnnouncement(HttpServletRequest request, Long id) {
        if (!isAdmin(request)) { // 관리자 권한 확인
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }

        announcementRepository.deleteById(id);
    }

    private Member findUser(String token) {
        String info = jwtProvider.getUsername(token);
        return memberRepository.findByPhoneNum(info);
    }



}
