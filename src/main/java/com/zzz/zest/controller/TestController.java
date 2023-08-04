package com.zzz.zest.controller;

import com.zzz.zest.wechat.WeChatService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Resource
    private WeChatService weChatService;

    @RequestMapping("/test")
    public String test() {
        return "test001";
    }

    @RequestMapping("/token")
    public String tokenTest() {
        return weChatService.getAccessToken();
    }




}
