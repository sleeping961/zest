package com.zzz.zest.init;


import com.zzz.zest.wechat.WeChatService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class InitService {

    @Resource
    private WeChatService weChatService;

    @PostConstruct
    void tokenInit() {
        String accessToken = weChatService.getAccessToken();
        log.info("当前服务器token:{}", accessToken);
    }


}
