package pl.com.dolittle.mkelo.config;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import pl.com.dolittle.mkelo.config.conditions.AWSPropertiesCondition;

@Configuration
@Conditional(AWSPropertiesCondition.class)
public class AWSConfig {

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.EU_NORTH_1)
                .build();
    }
}
