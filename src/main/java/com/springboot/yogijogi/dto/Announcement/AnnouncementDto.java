package com.springboot.yogijogi.dto.Announcement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AnnouncementDto {
    private String title;
    private String content;

}
