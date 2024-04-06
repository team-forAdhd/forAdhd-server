package com.project.foradhd.domain.auth.business.userdetails.impl;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.project.foradhd.domain.user.persistence.entity.User;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class OAuth2UserImpl extends DefaultOAuth2User {

    private final String userId;
    private final String email;

    public OAuth2UserImpl(Map<String, Object> attributes, String nameAttributeKey, User user) {
        super(createAuthorityList(user.getAuthority()), attributes, nameAttributeKey);
        this.userId = user.getId();
        this.email = user.getEmail();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<String> authorities = super.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        return AuthorityUtils.createAuthorityList(authorities);
    }
}
