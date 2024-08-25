package com.project.foradhd.global.service;

import com.project.foradhd.global.enums.EmailTemplate;

import java.util.Map;

public interface EmailService {

    void sendEmail(EmailTemplate emailTemplate, Map<String, Object> variables, String... to);
}
