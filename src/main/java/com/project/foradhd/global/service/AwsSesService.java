package com.project.foradhd.global.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@Service
public class AwsSesService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final TemplateEngine templateEngine;

    @Value("${aws.ses.from}")
    private String from;

    public void sendEmail(String template, String subject, Map<String, Object> variables, String... to) {
        String content = templateEngine.process(template, getContext(variables));
        SendEmailRequest sendEmailRequest = getSendEmailRequest(subject, content, to);
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
