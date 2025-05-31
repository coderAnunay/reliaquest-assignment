package com.reliaquest.api.client;

import com.reliaquest.api.dto.EmployeeApiResponseWrapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * A reusable API client that encapsulates WebClient-based calls to the upstream Employee API.
 * Simplifies service layer testing.
 */
@Component
public class EmployeeApiClient {

    private final WebClient employeeApiClient;

    public EmployeeApiClient(WebClient employeeApiClient) {
        this.employeeApiClient = employeeApiClient;
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
    public <T> T get(String uriTemplate, Object[] uriVars, ParameterizedTypeReference<EmployeeApiResponseWrapper<T>> type) {
        EmployeeApiResponseWrapper<T> responseWrapper = employeeApiClient.get()
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
         EmployeeApiResponseWrapper<T> responseWrapper = employeeApiClient.get()
                .retrieve()
                .bodyToMono(type)
                .block();

         return responseWrapper != null ? responseWrapper.getData() : null;
    }
}
