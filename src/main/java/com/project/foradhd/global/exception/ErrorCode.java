package com.project.foradhd.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //auth
    NOT_SUPPORTED_SNS_TYPE(BAD_REQUEST, "지원하지 않는 SNS 타입입니다."),
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "유효하지 않은 인증 토큰입니다."),
    NOT_MATCH_USERNAME_PASSWORD(UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),

    //user
    NOT_FOUND_USER(NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_USER_PROFILE(NOT_FOUND, "존재하지 않는 유저 프로필입니다."),
    ALREADY_EXISTS_EMAIL(CONFLICT, "이미 가입한 이메일입니다."),
    ALREADY_EXISTS_NICKNAME(CONFLICT, "이미 사용 중인 닉네임입니다."),
    NOT_FOUND_TERMS(NOT_FOUND, "존재하지 않는 이용약관입니다."),
    NOT_FOUND_PUSH_NOTIFICATION_APPROVAL(NOT_FOUND, "존재하지 않는 푸시 알림 동의 항목입니다."),
    REQUIRED_TERMS_APPROVAL(BAD_REQUEST, "필수 이용 약관에 동의해야 합니다."),
    EMAIL_AUTH_TIMEOUT(BAD_REQUEST, "이메일 인증 가능 시간을 초과했습니다."),

    //hospital
    NOT_FOUND_HOSPITAL(NOT_FOUND, "존재하지 않는 병원입니다."),
    NOT_FOUND_DOCTOR(NOT_FOUND, "존재하지 않는 의사입니다."),
    NOT_FOUND_HOSPITAL_RECEIPT_REVIEW(NOT_FOUND, "존재하지 않는 영수증 리뷰입니다."),
    NOT_FOUND_HOSPITAL_BRIEF_REVIEW(NOT_FOUND, "존재하지 않는 간단 리뷰입니다."),
    FORBIDDEN_HOSPITAL_RECEIPT_REVIEW(FORBIDDEN, "영수증 리뷰에 접근할 수 있는 권한이 없습니다."),
    FORBIDDEN_HOSPITAL_BRIEF_REVIEW(FORBIDDEN, "간단 리뷰에 접근할 수 있는 권한이 없습니다."),

    //validation
    INVALID_REQUEST(BAD_REQUEST, "잘못된 요청입니다."),

    //system
    SYSTEM_ERROR(INTERNAL_SERVER_ERROR, "시스템 에러입니다.");

    private final HttpStatus status;
    private final String message;
}
