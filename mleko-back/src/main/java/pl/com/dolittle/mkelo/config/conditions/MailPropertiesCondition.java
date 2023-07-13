package pl.com.dolittle.mkelo.config.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MailPropertiesCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String mailUsername = context.getEnvironment().getProperty("spring.mail.username");
        String mailPassword = context.getEnvironment().getProperty("spring.mail.password");
        String mailHost = context.getEnvironment().getProperty("spring.mail.host");
        return mailUsername != null && !mailUsername.isBlank()
                && mailPassword != null && !mailPassword.isBlank()
                && mailHost != null && !mailHost.isBlank();
    }
}
