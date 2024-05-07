package com.karty.kartyjavatest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimiterConfig {
    private long bucketSize;
    private long refillDuration;
}
