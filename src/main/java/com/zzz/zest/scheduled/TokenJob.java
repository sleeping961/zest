package com.zzz.zest.scheduled;

import com.zzz.zest.cache.LocalCache;
import com.zzz.zest.wechat.WeChatService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenJob {

    @Resource
    private WeChatService weChatService;

    @Resource
    private LocalCache localCache;

    @Scheduled(fixedRate = 4 * 60 * 1000) // 每4分钟执行一次
    public void checkAndRefreshToken() {
        String expirationTimeStr = localCache.get("expiration_time");

        if (expirationTimeStr != null) {
            long expirationTime = Long.parseLong(expirationTimeStr);
            long currentTime = System.currentTimeMillis();

            // 如果Token过期时间小于5分钟，重新获取Token
            if (expirationTime - currentTime <= 5 * 60 * 1000) {
                // 调用获取Token的方法，会自动刷新缓存
                weChatService.getAccessToken();
            }
        }
    }

}
