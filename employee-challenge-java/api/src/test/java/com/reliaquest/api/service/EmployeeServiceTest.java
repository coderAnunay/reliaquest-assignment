package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.client.EmployeeApiClient;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.exception.ResourceNotFoundException;
import com.reliaquest.api.util.TestDataFactory;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService Unit Tests")
public class EmployeeServiceTest {

    @Mock
    private EmployeeApiClient employeeApiClient;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeDTO mockSingleEmployee = TestDataFactory.getTestEmployeeDTO();

    private List<EmployeeDTO> mockEmployees = TestDataFactory.getTestEmployeeDTOList(15);

    @Nested
    @DisplayName("EmployeeService - getEmployeeById()")
    class GetEmployeeByIdTests {

        @Test
        @DisplayName("should return employee when response is valid")
        void shouldReturnEmployee_whenResponseIsValid() {
            // Arrange
            when(employeeApiClient.get(anyString(), any(), any())).thenReturn(mockSingleEmployee);
            String id = mockSingleEmployee.getId();

            // Act
            EmployeeDTO result = employeeService.getEmployeeById(id);

            // Assert
            assertNotNull(result);
            assertEquals(mockSingleEmployee, result);
        }

        @Test
        @DisplayName("should propagate ResourceNotFoundException when upstream api returns 404")
        void shouldPropagateNotFound_whenUpstreamReturns404() {
            // Arrange
            when(employeeApiClient.get(anyString(), any(), any()))
                    .thenThrow(mock(WebClientResponseException.NotFound.class));
            String id = mockSingleEmployee.getId();

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(id));
        }

        @Test
        @DisplayName("should propagate RuntimeException on request failure")
        void shouldPropagateRuntimeException_whenRequestFails() {
            // Arrange
            when(employeeApiClient.get(anyString(), any(), any())).thenThrow(mock(WebClientRequestException.class));
            String id = mockSingleEmployee.getId();

            // Act & Assert
            assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(id));
        }
    }

    @Nested
    @DisplayName("EmployeeService - getAllEmployees()")
    class GetAllEmployeesTests {

        @Test
        @DisplayName("should return all employees when upstream api returns data")
        void shouldReturnAllEmployees_whenAvailable() {
            // Arrange
            when(employeeApiClient.get(any())).thenReturn(mockEmployees);

            // Act
            List<EmployeeDTO> result = employeeService.getAllEmployees();

            // Assert
            assertEquals(mockEmployees.size(), result.size());
            verify(employeeApiClient).get(any());
        }

        @Test
        @DisplayName("should propagate RuntimeException on upstream api failure")
        void shouldPropagateRuntimeException_whenUpstreamApiFails() {
            // Arrange
            when(employeeApiClient.get(any())).thenThrow(mock(WebClientRequestException.class));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> employeeService.getAllEmployees());
        }
    }

    @Nested
    @DisplayName("EmployeeService - getHighestSalaryOfEmployees()")
    class GetHighestSalaryTests {

        @Test
        @DisplayName("should return highest salary correctly from employees list")
        void shouldReturnHighestSalary() {
            // Arrange
            when(employeeApiClient.get(any())).thenReturn(mockEmployees);
            Integer expectedResult = TestDataFactory.getHighestSalary(mockEmployees);

            // Act
            Integer actualResult = employeeService.getHighestSalaryOfEmployees();

            // Assert
            assertEquals(expectedResult, actualResult);
        }

        @Test
        @DisplayName("should return zero value when list is empty")
        void shouldReturnZero_whenListEmpty() {
            // Arrange
            when(employeeApiClient.get(any())).thenReturn(Collections.emptyList());

            // Act
            Integer result = employeeService.getHighestSalaryOfEmployees();

            // Assert
            assertEquals(0, result);
        }

        @Test
        @DisplayName("should propagate RuntimeException on upstream api failure")
        void shouldPropagateRuntimeException_whenFails() {
            // Arrange
            when(employeeApiClient.get(any())).thenThrow(mock(WebClientRequestException.class));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> employeeService.getHighestSalaryOfEmployees());
        }
    }

    @Nested
    @DisplayName("EmployeeService - getTopTenHighestEarningEmployeeNames()")
    class GetTopTenHighestEarningEmployeeNamesTests {

        @Test
        @DisplayName("should return top 10 names in descending order of salary")
        void shouldReturnTopTenNamesInOrder() {
            // Arrange
            when(employeeApiClient.get(any())).thenReturn(mockEmployees);
            List<String> expectedNamesList = TestDataFactory.getTopTenHighestEarningEmployeeNames(mockEmployees);

            // Act
            List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();

            // Assert
            assertEquals(expectedNamesList.size(), result.size());
            assertEquals(expectedNamesList.get(0), result.get(0)); // highest first
            assertEquals(expectedNamesList.get(3), result.get(3)); // lowest last
        }

        @Test
        @DisplayName("should return empty list when upstream data is null")
        void shouldReturnEmptyList_whenNull() {
            // Arrange
            when(employeeApiClient.get(any())).thenReturn(null);

            // Act
            List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();

            // Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should propagate RuntimeException when upstream api fails")
        void shouldThrowRuntimeException_whenFails() {
            // Arrange
            when(employeeApiClient.get(any())).thenThrow(mock(WebClientRequestException.class));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> employeeService.getTopTenHighestEarningEmployeeNames());
        }
    }
}
