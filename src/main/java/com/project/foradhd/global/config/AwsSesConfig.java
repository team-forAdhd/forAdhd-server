package com.project.foradhd.global.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsSesConfig {

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        //local 환경: EnvironmentVariableCredentialsProvider 동작 (AWS_ACCESS_KEY_ID, AWS_SECRET_KEY 환경 변수로 credentials 주입)
        //dev, prd 환경: InstanceProfileCredentialsProvider 동작 (AmazonSESFullAccess 권한을 가진 EC2 IAM Role을 통해 credentials 주입)
        DefaultAWSCredentialsProviderChain credentialsProvider = new DefaultAWSCredentialsProviderChain();
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }
}
