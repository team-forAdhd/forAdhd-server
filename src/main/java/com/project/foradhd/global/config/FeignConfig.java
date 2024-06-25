package com.project.foradhd.global.config;

import com.project.foradhd.ForadhdApplication;
import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = ForadhdApplication.class)
@Configuration
public class FeignConfig {

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 1500, 1);
    }
}
