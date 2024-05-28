package com.springboot.yogijogi.dto.Announcement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class AnnouncementSelectDto {
    private Long id;
    private String title;
    private int viewCount;
    private LocalDateTime create_At;

}
