package com.reliaquest.api.controller;

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

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        @DisplayName("should throw ClientBadRequestException when ID is null or blank")
        void shouldThrowClientBadRequestException_whenIdIsNullOrBlank() {
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
        void shouldThrowResourceNotFoundException_whenGivenIdDoesNotExist() {
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
