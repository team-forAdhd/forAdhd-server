package com.project.foradhd.domain.user.web.controller;

import com.project.foradhd.domain.user.business.dto.in.EmailUpdateData;
import com.project.foradhd.domain.user.business.dto.in.PasswordUpdateData;
import com.project.foradhd.domain.user.business.dto.in.ProfileUpdateData;
import com.project.foradhd.domain.user.business.dto.in.PushNotificationAgreeUpdateData;
import com.project.foradhd.domain.user.business.dto.in.SignUpData;
import com.project.foradhd.domain.user.business.dto.in.SnsSignUpData;
import com.project.foradhd.domain.user.business.dto.in.TermsApprovalsUpdateData;
import com.project.foradhd.domain.user.business.dto.out.UserProfileDetailsData;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.web.dto.request.EmailUpdateRequest;
import com.project.foradhd.domain.user.web.dto.request.NicknameCheckRequest;
import com.project.foradhd.domain.user.web.dto.request.PasswordUpdateRequest;
import com.project.foradhd.domain.user.web.dto.request.ProfileUpdateRequest;
import com.project.foradhd.domain.user.web.dto.request.PushNotificationAgreeUpdateRequest;
import com.project.foradhd.domain.user.web.dto.request.SignUpRequest;
import com.project.foradhd.domain.user.web.dto.request.SnsSignUpRequest;
import com.project.foradhd.domain.user.web.dto.request.TermsApprovalsUpdateRequest;
import com.project.foradhd.domain.user.web.dto.response.NicknameCheckResponse;
import com.project.foradhd.domain.user.web.dto.response.UserProfileDetailsResponse;
import com.project.foradhd.domain.user.web.mapper.UserMapper;
import com.project.foradhd.global.AuthUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/nickname-check")
    public ResponseEntity<NicknameCheckResponse> checkNickname(@RequestBody @Valid NicknameCheckRequest request) {
        boolean isValidNickname = userService.checkNickname(request.getNickname());
        return ResponseEntity.ok(new NicknameCheckResponse(isValidNickname));
    }

    @GetMapping
    public ResponseEntity<UserProfileDetailsResponse> getUserProfileDetails(@AuthUserId String userId) {
        UserProfileDetailsData userProfileDetailsData = userService.getUserProfileDetails(userId);
        UserProfileDetailsResponse response = userMapper.toUserProfileDetailsResponse(userProfileDetailsData);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        SignUpData signUpData = userMapper.toSignUpData(request);
        userService.signUp(signUpData);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/sns-sign-up")
    public ResponseEntity<Void> snsSignUp(@AuthUserId String userId,
        @RequestBody @Valid SnsSignUpRequest request) {
        SnsSignUpData snsSignUpData = userMapper.toSnsSignUpData(userId, request);
        userService.snsSignUp(userId, snsSignUpData);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/email-auth")
    public ResponseEntity<Void> authenticateEmail() {
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/email-auth")
    public ResponseEntity<Void> validateEmailAuth() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@AuthUserId String userId,
        @RequestBody @Valid ProfileUpdateRequest request) {
        ProfileUpdateData profileUpdateData = userMapper.toProfileUpdateData(request);
        userService.updateProfile(userId, profileUpdateData);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthUserId String userId,
        @RequestBody @Valid PasswordUpdateRequest request) {
        PasswordUpdateData passwordUpdateData = userMapper.toPasswordUpdateData(request);
        userService.updatePassword(userId, passwordUpdateData);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email")
    public ResponseEntity<Void> updateEmail(@AuthUserId String userId,
        @RequestBody @Valid EmailUpdateRequest request) {
        EmailUpdateData emailUpdateData = userMapper.toEmailUpdateData(request);
        userService.updateEmail(userId, emailUpdateData);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/push-notification-agree")
    public ResponseEntity<Void> updatePushNotificationAgree(@AuthUserId String userId,
        @RequestBody @Valid PushNotificationAgreeUpdateRequest request) {
        PushNotificationAgreeUpdateData pushNotificationAgreeUpdateData = userMapper.toPushNotificationAgreeUpdateData(request);
        userService.updatePushNotificationAgree(userId, pushNotificationAgreeUpdateData);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/terms-approvals")
    public ResponseEntity<Void> updateTermsApprovals(@AuthUserId String userId,
        @RequestBody @Valid TermsApprovalsUpdateRequest request) {
        TermsApprovalsUpdateData termsApprovalsUpdateData = userMapper.toTermsApprovalsUpdateData(userId, request);
        userService.updateTermsApprovals(termsApprovalsUpdateData);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@AuthUserId String userId) {
        return ResponseEntity.ok().build();
    }
}
