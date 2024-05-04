package com.project.foradhd.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailTemplate {

    USER_EMAIL_AUTH_TEMPLATE("emailAuth", "ForA 이메일 인증을 완료해주세요.");

    private final String template;
    private final String subject;
}
