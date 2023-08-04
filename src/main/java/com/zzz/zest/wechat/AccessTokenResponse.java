package com.zzz.zest.wechat;


import lombok.Data;

@Data
public class AccessTokenResponse {

    private String access_token;

    private Long expires_in;

}
