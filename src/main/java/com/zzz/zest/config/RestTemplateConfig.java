package com.zzz.zest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 使用HttpComponentsClientHttpRequestFactory来设置连接超时和读取超时等属性
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 设置连接超时时间为5秒
        factory.setConnectionRequestTimeout(5000);    // 设置读取超时时间为5秒

        return new RestTemplate(factory);
    }

}
