package com.project.foradhd.domain.user.web.controller;

import com.project.foradhd.domain.user.business.dto.in.ProfileUpdateData;
import com.project.foradhd.domain.user.business.dto.in.SignUpData;
import com.project.foradhd.domain.user.business.dto.in.SnsSignUpData;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.web.dto.request.ProfileUpdateRequest;
import com.project.foradhd.domain.user.web.dto.request.SignUpRequest;
import com.project.foradhd.domain.user.web.dto.request.SnsSignUpRequest;
import com.project.foradhd.domain.user.web.mapper.UserMapper;
import com.project.foradhd.global.AuthUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        SignUpData signUpData = userMapper.toSignUpData(request);
        userService.signUp(signUpData, request.getPassword());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/sns-sign-up")
    public ResponseEntity<Void> snsSignUp(@AuthUserId String userId,
        @RequestBody @Valid SnsSignUpRequest request) {
        SnsSignUpData snsSignUpData = userMapper.toSnsSignUpData(userId, request);
        userService.snsSignUp(userId, snsSignUpData);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@AuthUserId String userId,
        @RequestBody @Valid ProfileUpdateRequest request) {
        ProfileUpdateData profileUpdateData = userMapper.toProfileUpdateData(request);
        userService.updateProfile(userId, profileUpdateData);
        return ResponseEntity.ok().build();
    }
}
