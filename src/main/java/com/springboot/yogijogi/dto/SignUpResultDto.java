package com.springboot.yogijogi.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpResultDto {

    private boolean success;
    private int code;
    private String msg;

}
