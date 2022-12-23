package pl.com.dolittle.mkelo.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.services.EmailService;

@Service
@Slf4j
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private JavaMailSender emailSender;

    @Override
    @Async
    public void send(String receiver, String subject, String content) {
        var message = new SimpleMailMessage();
        message.setFrom("noreply@izb-mail.dolittle.com.pl");
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
        log.info("Sending secret to {}", receiver);
    }
}
