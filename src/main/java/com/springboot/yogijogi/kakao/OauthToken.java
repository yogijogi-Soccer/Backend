package com.springboot.yogijogi.kakao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@Getter
@Setter
public class OauthToken {
    private String id_token;
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;


}
