package pl.com.dolittle.mkelo.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("email-smtp.eu-central-1.amazonaws.com");
        mailSender.setPort(587);
        mailSender.setUsername("AKIAQLP52OJMOC3HOPFB");
        mailSender.setPassword("BJhNqLY2g5JPDBX/cUAPPqphtsLnJ1YPlK+frbsAuLh/");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
