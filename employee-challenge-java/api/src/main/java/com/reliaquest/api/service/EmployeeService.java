package com.reliaquest.api.service;

import static com.reliaquest.api.common.Constants.*;

import com.reliaquest.api.client.EmployeeApiClient;
import com.reliaquest.api.dto.CreateEmployeeDTO;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.exception.ResourceNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeApiClient employeeApiClient;

    public EmployeeService(final EmployeeApiClient employeeApiClient) {
        this.employeeApiClient = employeeApiClient;
    }

    public EmployeeDTO getEmployeeById(final String id) {
        try {
            EmployeeDTO employee = employeeApiClient.get(
                    GET_EMPLOYEE_BY_ID_URI, new Object[] {id}, new ParameterizedTypeReference<>() {});
            return employee;
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
            List<EmployeeDTO> employees = employeeApiClient.get(new ParameterizedTypeReference<>() {});
            return employees;
        } catch (WebClientRequestException ex) {
            LOGGER.error(INTERNAL_SERVER_ERROR, ex);
            throw new RuntimeException(INTERNAL_SERVER_ERROR, ex);
        }
    }

    public List<EmployeeDTO> getEmployeesByNameSearch(final String searchString) {
        try {
            List<EmployeeDTO> employees = employeeApiClient.get(new ParameterizedTypeReference<>() {});

            if (employees == null) {
                return Collections.emptyList();
            }

            String lowerSearch = searchString.toLowerCase();

            return employees.stream()
                    .filter(e -> e.getEmployeeName() != null
                            && e.getEmployeeName().toLowerCase().contains(lowerSearch))
                    .toList();
        } catch (WebClientRequestException ex) {
            LOGGER.error(INTERNAL_SERVER_ERROR, ex);
            throw new RuntimeException(INTERNAL_SERVER_ERROR, ex);
        }
    }

    public Integer getHighestSalaryOfEmployees() {
        try {
            List<EmployeeDTO> employees = employeeApiClient.get(new ParameterizedTypeReference<>() {});

            if (employees == null) {
                return 0;
            }

            return employees.stream()
                    .map(EmployeeDTO::getEmployeeSalary)
                    .filter(employeeSalary -> employeeSalary != null)
                    .max(Integer::compareTo)
                    .orElse(0);
        } catch (WebClientRequestException ex) {
            LOGGER.error(INTERNAL_SERVER_ERROR, ex);
            throw new RuntimeException(INTERNAL_SERVER_ERROR, ex);
        }
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        try {
            List<EmployeeDTO> employees = employeeApiClient.get(new ParameterizedTypeReference<>() {});

            if (employees == null) {
                return Collections.emptyList();
            }

            return employees.stream()
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

    public EmployeeDTO createEmployee(final CreateEmployeeDTO input) {
        return null;
    }

    public String deleteEmployeeById(final String id) {
        return null;
    }
}
