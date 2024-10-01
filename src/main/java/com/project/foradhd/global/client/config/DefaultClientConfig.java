package com.project.foradhd.global.client.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultClientConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        //모든 Feign Client에 대해 FULL 로그 레벨 적용
        return Logger.Level.FULL;
    }
}
