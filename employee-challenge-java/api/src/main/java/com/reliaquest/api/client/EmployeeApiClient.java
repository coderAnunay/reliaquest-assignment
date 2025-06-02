package com.reliaquest.api.client;

import com.reliaquest.api.dto.EmployeeApiResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * A reusable API client that encapsulates WebClient-based calls to the upstream Employee API.
 * Simplifies service layer testing.
 */
@Component
public class EmployeeApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeApiClient.class);

    private final WebClient employeeApiClient;

    public EmployeeApiClient(WebClient webClient) {
        this.employeeApiClient = webClient;
    }
    /**
     * Performs a GET call to the given URI with path variables and returns the `data` field
     * from the upstream response, unwrapped from the response wrapper.
     * @param uriTemplate the relative URI (e.g. "/{id}")
     * @param uriVars path variables to interpolate in the URI
     * @param type type reference for the wrapped response
     * @param <T> the type of the unwrapped data object
     * @return the unwrapped data, or null if the response is empty
     */
    public <T> T get(
            String uriTemplate, Object[] uriVars, ParameterizedTypeReference<EmployeeApiResponseWrapper<T>> type) {
        LOGGER.debug("EmployeeApiClient - GET request to URI: [{}]", uriTemplate);
        EmployeeApiResponseWrapper<T> responseWrapper = employeeApiClient
                .get()
                .uri(uriTemplate, uriVars)
                .retrieve()
                .bodyToMono(type)
                .block();

        return responseWrapper != null ? responseWrapper.getData() : null;
    }

    /**
     * Performs a GET call to the base URI (no path parameters) and returns the `data` field
     * from the upstream response, unwrapped from the response wrapper.
     * @param type type reference for the wrapped response
     * @param <T> the type of the unwrapped data object
     * @return the unwrapped data, or null if the response is empty
     */
    public <T> T get(ParameterizedTypeReference<EmployeeApiResponseWrapper<T>> type) {
        LOGGER.debug("EmployeeApiClient - GET request to base URI");
        EmployeeApiResponseWrapper<T> responseWrapper =
                employeeApiClient.get().retrieve().bodyToMono(type).block();

        return responseWrapper != null ? responseWrapper.getData() : null;
    }

    /**
     * Performs a POST call with a request body and returns the `data` field from the response.
     */
    public <T, R> T post(R requestBody, ParameterizedTypeReference<EmployeeApiResponseWrapper<T>> type) {
        LOGGER.debug("EmployeeApiClient - POST request with body: [{}]", requestBody);
        EmployeeApiResponseWrapper<T> responseWrapper = employeeApiClient
                .post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(type)
                .block();

        return responseWrapper != null ? responseWrapper.getData() : null;
    }

    /**
     * Performs a DELETE call with path variables and returns the `data` field from the response.
     */
    public <T, R> T delete(R requestBody, ParameterizedTypeReference<EmployeeApiResponseWrapper<T>> type) {
        LOGGER.debug("EmployeeApiClient - DELETE request with body: [{}]", requestBody);
        EmployeeApiResponseWrapper<T> responseWrapper = employeeApiClient
                .method(HttpMethod.DELETE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(type)
                .block();

        return responseWrapper != null ? responseWrapper.getData() : null;
    }
}
