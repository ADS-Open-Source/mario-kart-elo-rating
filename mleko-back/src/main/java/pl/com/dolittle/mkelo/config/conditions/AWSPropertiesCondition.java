package pl.com.dolittle.mkelo.config.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class AWSPropertiesCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String accessKey = context.getEnvironment().getProperty("cloud.aws.credentials.access-key");
        String secretKey = context.getEnvironment().getProperty("cloud.aws.credentials.secret-key");
        String profileName = context.getEnvironment().getProperty("cloud.aws.credentials.profile-name");
        return accessKey != null && !accessKey.isBlank()
                && secretKey != null && !secretKey.isBlank()
                && profileName != null && !profileName.isBlank();
    }
}
