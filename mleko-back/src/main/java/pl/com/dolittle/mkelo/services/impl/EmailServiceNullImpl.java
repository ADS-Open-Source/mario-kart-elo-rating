package pl.com.dolittle.mkelo.services.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.exception.EmailConfigurationNotProvidedException;
import pl.com.dolittle.mkelo.services.EmailService;

@Service
@Slf4j
@ConditionalOnMissingBean(EmailServiceImpl.class)
public class EmailServiceNullImpl implements EmailService, InitializingBean {

    @Override
    @Async
    public void send(String receiver, String subject, String content) {
        log.error("email configuration is missing. Mail sending unavailable");
        log.info("Content: {}", content);
        throw new EmailConfigurationNotProvidedException();
    }

    @Override
    public void afterPropertiesSet() {
        log.error("email service unavailable");
    }
}
