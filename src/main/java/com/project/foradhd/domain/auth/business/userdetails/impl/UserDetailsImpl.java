package com.project.foradhd.domain.auth.business.userdetails.impl;

import com.project.foradhd.domain.user.persistence.entity.User;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

    private final String userId;

    public UserDetailsImpl(User user) {
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("USER")));
        this.userId = user.getId();
    }
}
