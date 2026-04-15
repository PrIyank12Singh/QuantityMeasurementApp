package com.example.quantity.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
    name = "eureka.client.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class DotenvConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .ignoreIfMissing()
                .load();
    }
}