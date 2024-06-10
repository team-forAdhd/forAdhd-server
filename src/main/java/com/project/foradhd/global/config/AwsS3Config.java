package com.project.foradhd.global.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 amazonS3Client() {
        //local 환경: EnvironmentVariableCredentialsProvider 동작 (AWS_ACCESS_KEY_ID, AWS_SECRET_KEY 환경 변수로 credentials 주입)
        //dev, prd 환경: InstanceProfileCredentialsProvider 동작 (AmazonSESFullAccess 권한을 가진 EC2 IAM Role을 통해 credentials 주입)
        DefaultAWSCredentialsProviderChain credentialsProvider = new DefaultAWSCredentialsProviderChain();
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(region)
                .build();
    }
}
