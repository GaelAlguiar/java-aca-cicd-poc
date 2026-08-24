package com.example.aca.info;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppInfoProperties(
        String serviceName,
        String environment,
        String message
) {
}
