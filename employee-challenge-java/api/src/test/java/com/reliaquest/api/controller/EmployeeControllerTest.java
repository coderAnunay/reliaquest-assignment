package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.exception.ClientBadRequestException;
import com.reliaquest.api.exception.ResourceNotFoundException;
import com.reliaquest.api.service.EmployeeService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeController Tests")
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Nested
    @DisplayName("EmployeeController - getEmployeeById()")
    class GetEmployeeByIdTests {

        @Test
        @DisplayName("should return Employee when employee ID is valid")
        void shouldReturnEmployee_whenEmployeeIdIsValid() {
            // Arrange
            String validId = "123e4567-e89b-12d3-a456-425514174000";
            EmployeeDTO mockValidEmployee = new EmployeeDTO(
                    "123e4567-e89b-12d3-a456-425514174000",
                    "Jim Donga",
                    64350,
                    28,
                    "Construction Field Staff",
                    "jim@company.com");
            when(employeeService.getEmployeeById(validId)).thenReturn(mockValidEmployee);

            // Act
            ResponseEntity<EmployeeDTO> response = employeeController.getEmployeeById(validId);

            // Assert
            assertEquals(200, response.getStatusCode().value());
            assertEquals(mockValidEmployee, response.getBody());
            verify(employeeService).getEmployeeById(validId);
        }

        @Test
        @DisplayName("should throw ClientBadRequestException when employee ID is null or blank")
        void shouldThrowClientBadRequestException_whenEmployeeIdIsNullOrBlank() {
            // Arrange
            String nullId = null;
            String blankId = "";

            // Act & Assert
            assertThrows(ClientBadRequestException.class, () -> employeeController.getEmployeeById(nullId));

            assertThrows(ClientBadRequestException.class, () -> employeeController.getEmployeeById(blankId));
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when employee ID does not exist")
        void shouldThrowResourceNotFoundException_whenEmployeeIdDoesNotExist() {
            // Arrange
            String notExistingId = "123e4567-e89b-12d3-a456-426614174000";

            // Act
            when(employeeService.getEmployeeById(notExistingId))
                    .thenThrow(new ResourceNotFoundException("Employee not found"));

            // Assert
            assertThrows(ResourceNotFoundException.class, () -> employeeController.getEmployeeById(notExistingId));
        }
    }

    @Nested
    @DisplayName("EmployeeController - getAllEmployees()")
    class GetAllEmployeesTests {

        @Test
        @DisplayName("should return all employees when employees exist")
        void shouldReturnAllEmployees_whenEmployeesExist() {
            // Arrange
            List<EmployeeDTO> mockEmployees = List.of(
                    new EmployeeDTO(
                            "4a76c411-e94b-4cff-bbc2-a746249f175d",
                            "Reid Graham",
                            90155,
                            25,
                            "Construction Producer",
                            "konklux@company.com"),
                    new EmployeeDTO(
                            "bfa01553-68f7-49c4-b0g7-780002e9ebf2",
                            "Jim Donga",
                            64350,
                            28,
                            "Construction Field Staff",
                            "jim@company.com"));
            when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

            // Act
            ResponseEntity<List<EmployeeDTO>> response = employeeController.getAllEmployees();

            // Assert
            assertNotNull(response);
            assertNotNull(response.getBody());
            assertEquals(200, response.getStatusCode().value());
            assertEquals(mockEmployees, response.getBody());
            assertEquals(mockEmployees.size(), response.getBody().size());
            verify(employeeService).getAllEmployees();
        }

        @Test
        @DisplayName("should return empty list with 200 when no employees exist")
        void shouldReturnEmptyList_whenNoEmployeesExist() {
            // Arrange
            when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

            // Act
            ResponseEntity<List<EmployeeDTO>> response = employeeController.getAllEmployees();

            // Assert
            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(employeeService).getAllEmployees();
        }

        @Test
        @DisplayName("should propagate exception if service layer throws error")
        void shouldPropagateException_whenServiceThrowsError() {
            // Arrange
            when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("mock employee api failed"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> employeeController.getAllEmployees());
            verify(employeeService).getAllEmployees();
        }
    }

    @Nested
    @DisplayName("EmployeeController - getHighestSalaryOfEmployees()")
    class GetHighestSalaryTests {

        @Test
        @DisplayName("should return highest salary out of all employees")
        void shouldReturnHighestSalary_whenEmployeesExist() {
            // Arrange
            Integer expectedHighestSalary = 208820;
            when(employeeService.getHighestSalaryOfEmployees()).thenReturn(expectedHighestSalary);

            // Act
            ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

            // Assert
            assertEquals(200, response.getStatusCode().value());
            assertEquals(expectedHighestSalary, response.getBody());
            verify(employeeService).getHighestSalaryOfEmployees();
        }

        @Test
        @DisplayName("should propagate exception when service layer throws error")
        void shouldPropagateException_whenServiceThrowsError() {
            // Arrange
            when(employeeService.getHighestSalaryOfEmployees())
                    .thenThrow(new RuntimeException("Mock employee api failed"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> employeeController.getHighestSalaryOfEmployees());
            verify(employeeService).getHighestSalaryOfEmployees();
        }
    }
}
