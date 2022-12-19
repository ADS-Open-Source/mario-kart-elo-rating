package pl.com.dolittle.mkelo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    protected static final List<String> ALLOWED_HEADERS = Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept",
            "Authorization", "Access-Control-Allow-Credentials", "Access-Control-Allow-Headers", "Access-Control-Allow-Methods",
            "Access-Control-Allow-Origin", "Access-Control-Expose-Headers", "Access-Control-Max-Age",
            "Access-Control-Request-Headers", "Access-Control-Request-Method", "Age", "Allow", "Alternates",
            "Content-Type", "Content-Range", "Content-Disposition", "Content-Description");
    protected static final List<String> ALLOWED_METHODS = Arrays.asList("GET", "POST");
    public static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:4200",
            "http://mleko.dolittle.com.pl",
            "http://mleko.deloitte.cyou");

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(ALLOWED_ORIGINS);
        corsConfiguration.setAllowedMethods(ALLOWED_METHODS);
        corsConfiguration.setAllowedHeaders(ALLOWED_HEADERS);
        corsConfiguration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
