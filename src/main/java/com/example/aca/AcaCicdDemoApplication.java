package com.example.aca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AcaCicdDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcaCicdDemoApplication.class, args);
    }
}
