package com.project.foradhd.config.user;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockTestUserSecurityContextFactory.class)
public @interface WithMockTestUser {

    String userId() default "testUserId";

    String[] authorities() default {"ROLE_USER"};
}
