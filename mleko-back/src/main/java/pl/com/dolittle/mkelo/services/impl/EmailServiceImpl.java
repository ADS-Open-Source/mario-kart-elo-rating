package pl.com.dolittle.mkelo.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.config.conditions.MailPropertiesCondition;
import pl.com.dolittle.mkelo.services.EmailService;

@Service
@Slf4j
@Conditional(MailPropertiesCondition.class)
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.sender}")
    private String senderAddress;

    @Override
    @Async
    public void send(String receiver, String subject, String content) {
        var message = new SimpleMailMessage();
        message.setFrom(senderAddress);
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
        log.info("Sending secret to {}", receiver);
    }
}
