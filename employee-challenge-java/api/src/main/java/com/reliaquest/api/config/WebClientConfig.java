package com.reliaquest.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient employeeApiClient(MockServerApiProperties props) {
        return WebClient.builder()
                .baseUrl(props.getEmployeeApiUrl())
                .build();
    }
}
