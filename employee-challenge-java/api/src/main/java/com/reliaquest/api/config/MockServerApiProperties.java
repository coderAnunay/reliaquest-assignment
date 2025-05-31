package com.reliaquest.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mock-employee-api")
public class MockServerApiProperties {

    private String host;

    private String path;

    public String getEmployeeApiUrl() {
        return host + path;
    }
}
