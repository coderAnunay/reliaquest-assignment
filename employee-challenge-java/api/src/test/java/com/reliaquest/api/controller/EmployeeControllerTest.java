package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.dto.CreateEmployeeDTO;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.exception.ClientBadRequestException;
import com.reliaquest.api.exception.ResourceNotFoundException;
import com.reliaquest.api.service.EmployeeService;
import com.reliaquest.api.util.TestDataFactory;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeController Unit Tests")
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeDTO mockSingleEmployee = TestDataFactory.getTestEmployeeDTO();

    private List<EmployeeDTO> mockEmployees = TestDataFactory.getTestEmployeeDTOList(15);

    @Nested
    @DisplayName("EmployeeController - getEmployeeById()")
    class GetEmployeeByIdTests {

        @Test
        @DisplayName("should return Employee when employee ID is valid")
        void shouldReturnEmployee_whenEmployeeIdIsValid() {
            // Arrange
            String validId = mockSingleEmployee.getId();
            when(employeeService.getEmployeeById(validId)).thenReturn(mockSingleEmployee);

            // Act
            ResponseEntity<EmployeeDTO> response = employeeController.getEmployeeById(validId);

            // Assert
            assertEquals(200, response.getStatusCode().value());
            assertEquals(mockSingleEmployee, response.getBody());
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
            Integer expectedHighestSalary = TestDataFactory.getHighestSalary(mockEmployees);
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

    @Nested
    @DisplayName("EmployeeController - getTopTenHighestEarningEmployeeNames()")
    class GetTopTenHighestEarnersTests {

        @Test
        @DisplayName("should return top 10 employee names when data exists")
        void shouldReturnTopTenNames_whenDataExists() {
            // Arrange
            List<String> topTen = TestDataFactory.getTopTenHighestEarningEmployeeNames(mockEmployees);
            when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(topTen);

            // Act
            ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

            // Assert
            assertEquals(200, response.getStatusCode().value());
            assertEquals(topTen, response.getBody());
            verify(employeeService).getTopTenHighestEarningEmployeeNames();
        }

        @Test
        @DisplayName("should propagate exception when service layer throws error")
        void shouldPropagateException_whenServiceThrowsError() {
            // Arrange
            when(employeeService.getTopTenHighestEarningEmployeeNames())
                    .thenThrow(new RuntimeException("Mock employee api failed"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> employeeController.getTopTenHighestEarningEmployeeNames());
            verify(employeeService).getTopTenHighestEarningEmployeeNames();
        }
    }

    @Nested
    @DisplayName("EmployeeController - getEmployeesByNameSearch()")
    class GetEmployeesByNameSearchTests {

        @Test
        @DisplayName("should return employees whose name contains the search string")
        void shouldReturnMatchingEmployees() {
            // Arrange
            EmployeeDTO tom1 = new EmployeeDTO("1", "Tom Daryl", 75000, 32, "Engineer", "tomdaryl@company.com");
            EmployeeDTO tom2 = new EmployeeDTO("2", "Tom Finsey", 80000, 29, "Analyst", "tomfinsey@company.com");

            final String searchQuery = "Tom";
            List<EmployeeDTO> expectedMatches = List.of(tom1, tom2);
            when(employeeService.getEmployeesByNameSearch(searchQuery)).thenReturn(expectedMatches);

            // Act
            ResponseEntity<List<EmployeeDTO>> response = employeeController.getEmployeesByNameSearch(searchQuery);

            // Assert
            assertEquals(200, response.getStatusCode().value());
            assertEquals(expectedMatches, response.getBody());
            verify(employeeService).getEmployeesByNameSearch(searchQuery);
        }

        @Test
        @DisplayName("should return empty list when no employees match the search")
        void shouldReturnEmptyList_whenNoMatches() {
            // Arrange
            final String searchQuery = "Tom";
            when(employeeService.getEmployeesByNameSearch(searchQuery)).thenReturn(List.of());

            // Act
            ResponseEntity<List<EmployeeDTO>> response = employeeController.getEmployeesByNameSearch(searchQuery);

            // Assert
            assertEquals(200, response.getStatusCode().value());
            assertTrue(response.getBody().isEmpty());
            verify(employeeService).getEmployeesByNameSearch(searchQuery);
        }

        @Test
        @DisplayName("should throw Client Bad Request when search string is blank or empty")
        void shouldThrowClientBadRequest_whenSearchStringBlankOrEmpty() {
            // Arrange
            String invalidSearchBlank = " ";
            String invalidSearchEmpty = "";

            // Act & Assert
            assertThrows(ClientBadRequestException.class, () -> {
                employeeController.getEmployeesByNameSearch(invalidSearchBlank);
            });

            assertThrows(ClientBadRequestException.class, () -> {
                employeeController.getEmployeesByNameSearch(invalidSearchEmpty);
            });

            verify(employeeService, never()).getEmployeesByNameSearch(any());
        }
    }

    @Nested
    @DisplayName("EmployeeController - createEmployee() and deleteEmployeeById()")
    class CreateAndDeleteEmployeeTests {

        @Test
        @DisplayName("should create employee successfully")
        void shouldCreateEmployeeSuccessfully() {
            // Arrange
            CreateEmployeeDTO input = new CreateEmployeeDTO();
            input.setName(mockSingleEmployee.getEmployeeName());
            input.setSalary(mockSingleEmployee.getEmployeeSalary());
            input.setAge(mockSingleEmployee.getEmployeeAge());
            input.setTitle(mockSingleEmployee.getEmployeeTitle());
            input.setEmail(mockSingleEmployee.getEmployeeEmail());

            when(employeeService.createEmployee(input)).thenReturn(mockSingleEmployee);

            // Act
            ResponseEntity<EmployeeDTO> response = employeeController.createEmployee(input);

            // Assert
            assertEquals(201, response.getStatusCode().value());
            assertEquals(mockSingleEmployee, response.getBody());
            verify(employeeService).createEmployee(input);
        }

        @Test
        @DisplayName("should delete employee by ID and return the name of the deleted employee")
        void shouldDeleteEmployeeSuccessfullyAndReturnName() {
            // Arrange
            when(employeeService.deleteEmployeeById(anyString())).thenReturn(mockSingleEmployee.getEmployeeName());

            // Act
            ResponseEntity<String> response = employeeController.deleteEmployeeById(mockSingleEmployee.getId());

            // Assert
            assertEquals(200, response.getStatusCode().value());
            assertEquals(mockSingleEmployee.getEmployeeName(), response.getBody());
            verify(employeeService).deleteEmployeeById(mockSingleEmployee.getId());
        }
    }
}
