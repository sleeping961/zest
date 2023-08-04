package com.zzz.zest.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@RestController
public class WeChatController {

    @Value("${wechat.token:sleepingToken}")
    private String token;

    @GetMapping("/wechat")
    public String validateToken(@RequestParam("signature") String signature,
                                @RequestParam("timestamp") String timestamp,
                                @RequestParam("nonce") String nonce,
                                @RequestParam("echostr") String echostr) {
        // 将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = {token, timestamp, nonce};
        Arrays.sort(arr);

        // 将三个参数字符串拼接成一个字符串
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            sb.append(s);
        }

        // 使用SHA1加密算法对拼接后的字符串进行加密
        String encrypted = sha1(sb.toString());

        // 将加密后的结果与微信传来的signature进行对比，如果一致，说明验证通过
        if (encrypted.equals(signature)) {
            return echostr;
        } else {
            return "Invalid request";
        }
    }

    private String sha1(String input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            byte[] digest = sha1.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
