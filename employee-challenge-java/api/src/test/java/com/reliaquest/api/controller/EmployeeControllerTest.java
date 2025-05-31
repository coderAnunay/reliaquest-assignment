package com.reliaquest.api.controller;

import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.exception.ClientBadRequestException;
import com.reliaquest.api.exception.ResourceNotFoundException;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeController Tests")
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Nested
    @DisplayName("When calling getEmployeeById()")
    class GetEmployeeByIdTests {

        @Test
        @DisplayName("should return Employee when employee ID is valid")
        void shouldReturnEmployee_whenEmployeeIdIsValid() {
            // Arrange
            String validId = "123e4567-e89b-12d3-a456-425514174000";
            EmployeeDTO mockValidEmployee = new EmployeeDTO();
            when(employeeService.getEmployeeById(validId))
                    .thenReturn(mockValidEmployee);

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
            assertThrows(
                    ClientBadRequestException.class,
                    () -> employeeController.getEmployeeById(nullId)
            );

            assertThrows(
                    ClientBadRequestException.class,
                    () -> employeeController.getEmployeeById(blankId)
            );
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
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> employeeController.getEmployeeById(notExistingId)
            );
        }
    }

}