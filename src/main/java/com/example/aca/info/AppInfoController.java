package com.example.aca.info;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
public class AppInfoController {

    private final AppInfoProperties properties;

    public AppInfoController(AppInfoProperties properties) {
        this.properties = properties;
    }

    @GetMapping
    public AppInfoResponse getInfo() {
        return new AppInfoResponse(
                properties.serviceName(),
                properties.environment(),
                properties.message()
        );
    }

    public record AppInfoResponse(String service, String environment, String message) {
    }
}
