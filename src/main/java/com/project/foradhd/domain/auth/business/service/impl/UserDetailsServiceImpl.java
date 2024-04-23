package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.userdetails.impl.UserDetailsImpl;
import com.project.foradhd.domain.auth.persistence.entity.AuthPassword;
import com.project.foradhd.domain.auth.persistence.repository.AuthPasswordRepository;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthPasswordRepository authPasswordRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다."));
        AuthPassword authPassword = authPasswordRepository.findByUserId(user.getId())
            .orElseThrow(() -> new UsernameNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다."));
        return new UserDetailsImpl(user, authPassword.getPassword());
    }
}
