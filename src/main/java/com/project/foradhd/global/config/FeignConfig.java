package com.project.foradhd.global.config;

import com.project.foradhd.ForadhdApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = ForadhdApplication.class)
@Configuration
public class FeignConfig {
}
