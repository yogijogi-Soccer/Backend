package com.springboot.yogijogi.dto.SignUpIn;

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
