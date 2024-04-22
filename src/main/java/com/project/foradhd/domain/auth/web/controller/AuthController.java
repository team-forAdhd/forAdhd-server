package com.project.foradhd.domain.auth.web.controller;

import com.project.foradhd.domain.auth.business.dto.out.AuthTokenData;
import com.project.foradhd.domain.auth.business.service.AuthService;
import com.project.foradhd.domain.auth.web.dto.request.TokenReissueRequest;
import com.project.foradhd.domain.auth.web.dto.response.TokenReissueResponse;
import com.project.foradhd.domain.auth.web.mapper.AuthMapper;
import com.project.foradhd.global.AuthUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;

    @PutMapping("/reissue")
    public ResponseEntity<TokenReissueResponse> reissue(@RequestBody @Valid TokenReissueRequest request) {
        AuthTokenData authTokenData = authService.reissue(request.getAccessToken(), request.getRefreshToken());
        TokenReissueResponse response = authMapper.toTokenReissueResponse(authTokenData);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@AuthUserId String userId) {
        return ResponseEntity.ok().build();
    }
}
