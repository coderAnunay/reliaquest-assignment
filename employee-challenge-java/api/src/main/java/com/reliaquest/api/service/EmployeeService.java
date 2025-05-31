package com.reliaquest.api.service;

import static com.reliaquest.api.common.Constants.*;

import com.reliaquest.api.dto.EmployeeApiResponseWrapper;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.exception.ResourceNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    private final WebClient employeeApiClient;

    public EmployeeService(final WebClient employeeApiClient) {
        this.employeeApiClient = employeeApiClient;
    }

    public EmployeeDTO getEmployeeById(final String id) {
        try {
            EmployeeApiResponseWrapper<EmployeeDTO> responseWrapper = employeeApiClient
                    .get()
                    .uri(GET_EMPLOYEE_BY_ID_URI, id)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<EmployeeApiResponseWrapper<EmployeeDTO>>() {})
                    .block();
            return responseWrapper != null ? responseWrapper.getData() : null;
        } catch (WebClientResponseException.NotFound ex) {
            LOGGER.error(EMPLOYEE_NOT_FOUND, ex);
            throw new ResourceNotFoundException(EMPLOYEE_NOT_FOUND);
        } catch (WebClientRequestException ex) {
            LOGGER.error(INTERNAL_SERVER_ERROR, ex);
            throw new RuntimeException(INTERNAL_SERVER_ERROR, ex);
        }
    }

    public List<EmployeeDTO> getAllEmployees() {
        try {
            EmployeeApiResponseWrapper<List<EmployeeDTO>> responseWrapper = employeeApiClient
                    .get()
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<EmployeeApiResponseWrapper<List<EmployeeDTO>>>() {})
                    .block();
            return responseWrapper != null ? responseWrapper.getData() : Collections.emptyList();
        } catch (WebClientRequestException ex) {
            LOGGER.error(INTERNAL_SERVER_ERROR, ex);
            throw new RuntimeException(INTERNAL_SERVER_ERROR, ex);
        }
    }

    public List<EmployeeDTO> getEmployeesByNameSearch(final String searchString) {
        // TODO: call upstream API to search employees by name
        return List.of();
    }

    public Integer getHighestSalaryOfEmployees() {
        try {
            EmployeeApiResponseWrapper<List<EmployeeDTO>> responseWrapper = employeeApiClient
                    .get()
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<EmployeeApiResponseWrapper<List<EmployeeDTO>>>() {})
                    .block();

            if (responseWrapper == null || responseWrapper.getData() == null) {
                return 0;
            }

            return responseWrapper.getData().stream()
                    .filter(e -> e.getEmployeeSalary() != null)
                    .map(EmployeeDTO::getEmployeeSalary)
                    .max(Integer::compareTo)
                    .orElse(0);
        } catch (WebClientRequestException ex) {
            LOGGER.error(INTERNAL_SERVER_ERROR, ex);
            throw new RuntimeException(INTERNAL_SERVER_ERROR, ex);
        }
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        try {
            EmployeeApiResponseWrapper<List<EmployeeDTO>> responseWrapper = employeeApiClient
                    .get()
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<EmployeeApiResponseWrapper<List<EmployeeDTO>>>() {})
                    .block();

            if (responseWrapper == null || responseWrapper.getData() == null) {
                return Collections.emptyList();
            }

            return responseWrapper.getData().stream()
                    .filter(e -> e.getEmployeeSalary() != null && e.getEmployeeName() != null)
                    .sorted(Comparator.comparing(EmployeeDTO::getEmployeeSalary).reversed())
                    .limit(10)
                    .map(EmployeeDTO::getEmployeeName)
                    .toList();
        } catch (WebClientRequestException ex) {
            LOGGER.error(INTERNAL_SERVER_ERROR, ex);
            throw new RuntimeException(INTERNAL_SERVER_ERROR, ex);
        }
    }
}
