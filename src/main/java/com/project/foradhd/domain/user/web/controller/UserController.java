package com.project.foradhd.domain.user.web.controller;

import com.project.foradhd.domain.user.business.dto.SignUpDto;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.web.dto.request.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        SignUpDto.In signUpDto = new SignUpDto.In(request);
        userService.signUp(signUpDto, request.getPassword());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
