package com.reliaquest.api.service;

import com.reliaquest.api.dto.EmployeeApiResponseWrapper;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

import static com.reliaquest.api.common.Constants.*;

@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    private final WebClient employeeApiClient;

    public EmployeeService(final WebClient employeeApiClient) {
        this.employeeApiClient = employeeApiClient;
    }

    public EmployeeDTO getEmployeeById(final String id) {
        try {
            EmployeeApiResponseWrapper<EmployeeDTO> responseWrapper
                    = employeeApiClient.get()
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
        // TODO: call upstream API to get all employees
        return List.of();
    }

    public List<EmployeeDTO> getEmployeesByNameSearch(final String searchString) {
        // TODO: call upstream API to search employees by name
        return List.of();
    }

    public Integer getHighestSalaryOfEmployees() {
        // TODO: call upstream API to get the highest salary of employees
        return null;
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        // TODO: call upstream API to get the top ten highest earning employee names
        return List.of();
    }
}
