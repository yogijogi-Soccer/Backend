package com.springboot.yogijogi.service;

import com.springboot.yogijogi.dto.Announcement.AnnouncementDetailDto;
import com.springboot.yogijogi.dto.Announcement.AnnouncementDto;
import com.springboot.yogijogi.dto.Announcement.AnnouncementSelectDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AnnouncementService {

     AnnouncementDto saveAnnouncement(HttpServletRequest request, AnnouncementDto announcementDto );
     AnnouncementDto updateAnnouncement(HttpServletRequest request, Long id, AnnouncementDto announcementDto);
     List<AnnouncementSelectDto> selectAnnouncementList(HttpServletRequest request, Long id);
     AnnouncementDetailDto selectAnnouncementDetail(HttpServletRequest request, Long teamId, Long announcementId);

     void deleteAnnouncement(HttpServletRequest request, Long id);


    }
