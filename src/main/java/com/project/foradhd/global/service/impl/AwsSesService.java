package com.project.foradhd.global.service.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.project.foradhd.global.enums.EmailTemplate;
import com.project.foradhd.global.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@Service
public class AwsSesService implements EmailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final TemplateEngine templateEngine;

    @Value("${aws.ses.from}")
    private String from;

    @Override
    public void sendEmail(EmailTemplate emailTemplate, Map<String, Object> variables, String... to) {
        String content = templateEngine.process(emailTemplate.getTemplate(), getContext(variables));
        SendEmailRequest sendEmailRequest = getSendEmailRequest(emailTemplate.getSubject(), content, to);
        amazonSimpleEmailService.sendEmail(sendEmailRequest);
    }

    private Context getContext(Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return context;
    }

    private SendEmailRequest getSendEmailRequest(String subject, String content, String... to) {
        Destination destination = new Destination().withToAddresses(to);
        Content messageSubject = new Content().withCharset(UTF_8.name()).withData(subject);
        Body messageBody = new Body().withHtml(new Content().withCharset(UTF_8.name()).withData(content));
        Message message = new Message()
                .withSubject(messageSubject)
                .withBody(messageBody);
        return new SendEmailRequest()
                .withDestination(destination)
                .withSource(from)
                .withMessage(message);
    }
}
