package com.project.foradhd.config.user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockTestUserSecurityContextFactory implements WithSecurityContextFactory<WithMockTestUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockTestUser testUser) {
        String userId = testUser.userId();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(testUser.authorities());
        Authentication authentication = UsernamePasswordAuthenticationToken
                .authenticated(userId, null, authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
