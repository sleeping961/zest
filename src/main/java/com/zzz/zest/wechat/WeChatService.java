package com.zzz.zest.wechat;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zzz.zest.cache.LocalCache;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class WeChatService {

    @Value("${wechat.appId:wxe2f4b47678fd2d8d}")
    private String appId;

    @Value("${wechat.appSecret:81834ed8fe03f9cf769f14854cfdc9cb}")
    private String appSecret;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    LocalCache localCache;

    public String getAccessToken() {
        String cachedToken = localCache.get("access_token");
        String expirationTimeStr = localCache.get("expiration_time");

        if (cachedToken != null && expirationTimeStr != null) {
            long expirationTime = Long.parseLong(expirationTimeStr);
            long currentTime = System.currentTimeMillis();

            // 如果Token还有5分钟过期之内，则触发刷新
            if (expirationTime - currentTime > 5 * 60 * 1000) {
                return cachedToken;
            }
        }

        // 缓存中没有，或者Token即将过期，调用接口获取Access Token
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        log.info("getToken->response:{}", JSONObject.toJSONString(response));
        if (response.getStatusCode().is2xxSuccessful()) {
            AccessTokenResponse accessTokenResponse = JSONObject.parseObject(response.getBody(), new TypeReference<>(){});
            if (accessTokenResponse != null) {
                if (StringUtils.isNotBlank(accessTokenResponse.getAccess_token()) && accessTokenResponse.getExpires_in() != null) {

                    String accessToken = accessTokenResponse.getAccess_token();
                    long expiresIn = accessTokenResponse.getExpires_in();

                    // 将获取到的Access Token存入缓存，并设置过期时间
                    localCache.set("access_token", accessToken);
                    // 注意：expiresIn是秒数，这里将其转换为毫秒
                    long expirationTime = System.currentTimeMillis() + (expiresIn * 1000);
                    localCache.set("expiration_time", String.valueOf(expirationTime));
                    return accessToken;

                }
            }
        }
        // 处理异常情况
        return null;
    }
}
