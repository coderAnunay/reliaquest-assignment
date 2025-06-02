package com.reliaquest.api.integration;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Shared base class for integration testing of API endpoints.
 * Provides full application context and configures WebTestClient
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public abstract class AbstractIntegrationTest {}
