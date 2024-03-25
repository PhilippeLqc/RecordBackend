package com.recordbackend.Service;

import com.recordbackend.Dto.EmailDetailsDto;
import com.recordbackend.Model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class EmailService {

    private static final String EMAIL_FROM = "noreply@youtubeclone.com";

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final SecurityTokenService securityTokenService;

    public void sendEmailWithTemplate(EmailDetailsDto emailDetailsDto) throws MessagingException {
        Context emailContext = new Context();
        emailContext.setVariables(emailDetailsDto.getVariables());
        String htmlBody = thymeleafTemplateEngine.process(emailDetailsDto.getTemplate(), emailContext);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(EMAIL_FROM);
        helper.setTo(emailDetailsDto.getTo());
        helper.setSubject(emailDetailsDto.getSubject());
        helper.setText(htmlBody, true);
        helper.addInline("logo", new ClassPathResource("static/images/record.png"), "image/png");

        emailSender.send(message);
    }

    public void sendEmailVerification(User user) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("verificationLink", securityTokenService.generateVerificationLink(user));

        EmailDetailsDto emailDetails = EmailDetailsDto.builder()
                .template("email-verification")
                .to(user.getEmail())
                .subject("Vérification email")
                .variables(variables)
                .build();

        sendEmailWithTemplate(emailDetails);
    }
}
