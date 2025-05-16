package net.az3l1t.config;

import net.az3l1t.aspect.HttpLoggingAspect;
import net.az3l1t.config.properties.HttpLoggingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HttpLoggingProperties.class)
@ConditionalOnProperty(prefix = "http.logging", name = "enabled", havingValue = "true")
public class HttpLoggingAutoConfiguration {

    @Bean
    public HttpLoggingAspect httpLoggingAspect(HttpLoggingProperties httpLoggingProperties) {
        return new HttpLoggingAspect(httpLoggingProperties);
    }
}
