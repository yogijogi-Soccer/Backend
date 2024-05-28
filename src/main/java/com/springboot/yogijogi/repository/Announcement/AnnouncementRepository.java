package com.springboot.yogijogi.repository.Announcement;

import com.springboot.yogijogi.entity.Announcement;
import com.springboot.yogijogi.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement,Long>{
    // 특정 팀에 속한 공지사항을 조회하는 메서드
    List<Announcement> findByTeam(Team  team);


}
