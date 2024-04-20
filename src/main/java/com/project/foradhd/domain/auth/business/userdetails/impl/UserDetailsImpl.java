package com.project.foradhd.domain.auth.business.userdetails.impl;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.project.foradhd.domain.user.persistence.entity.User;
import lombok.Getter;

@Getter
public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

    private final String userId;

    public UserDetailsImpl(User user) {
        super(user.getEmail(), null, createAuthorityList(user.getAuthority()));
        this.userId = user.getId();
    }
}
