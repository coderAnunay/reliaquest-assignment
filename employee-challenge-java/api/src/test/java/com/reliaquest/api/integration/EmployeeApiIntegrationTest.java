package com.reliaquest.api.integration;

import static com.reliaquest.api.common.Constants.GET_EMPLOYEE_BY_ID_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.reliaquest.api.client.EmployeeApiClient;
import com.reliaquest.api.dto.CreateEmployeeDTO;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.util.TestDataFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class EmployeeApiIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeApiClient employeeApiClient;

    private List<EmployeeDTO> employees;

    @BeforeEach
    void setup() {
        employees = TestDataFactory.getTestEmployeeDTOList(15);
    }

    @Test
    @DisplayName("Integration test scenario: Create → Get → Search → Delete should work successfully")
    void shouldCreateAndFetchAndSearchAndDeleteEmployee() {

        // Arrange
        CreateEmployeeDTO input = TestDataFactory.getTestCreateEmployeeDTO("John Doe");
        EmployeeDTO createdEmployee = TestDataFactory.getTestEmployeeDTOFromCreateEmployeeDTO(input);
        when(employeeApiClient.post(eq(input), any())).thenReturn(createdEmployee);

        // Step 1: Create
        webTestClient
                .post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(input)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(EmployeeDTO.class)
                .isEqualTo(createdEmployee);

        // Arrange
        when(employeeApiClient.get(eq(GET_EMPLOYEE_BY_ID_URI), eq(new Object[] {createdEmployee.getId()}), any()))
                .thenReturn(createdEmployee);

        // Step 2: Fetch by ID
        webTestClient
                .get()
                .uri("/{id}", createdEmployee.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(EmployeeDTO.class)
                .isEqualTo(createdEmployee);

        // Arrange
        List<EmployeeDTO> employees = List.of(createdEmployee);
        when(employeeApiClient.get(any())).thenReturn(employees);

        // Step 3: Search by name
        webTestClient
                .get()
                .uri("/search/{search}", "john")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(EmployeeDTO.class)
                .hasSize(1)
                .contains(createdEmployee);

        // Arrange
        when(employeeApiClient.delete(any(), any())).thenReturn(Boolean.TRUE);

        // Step 4: Delete by ID
        webTestClient
                .delete()
                .uri("/{id}", createdEmployee.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo(input.getName());
    }

    @Test
    @DisplayName("Integration test scenario: Get All Employees should return all employees correctly")
    void shouldGetAllEmployeesSuccessfully() {
        // Arrange
        when(employeeApiClient.get(any())).thenReturn(employees);

        // Act
        List<EmployeeDTO> listOfEmployees = webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        // Assert
        assertNotNull(listOfEmployees);
        assertEquals(employees.size(), listOfEmployees.size());
        for (int i = 0; i < listOfEmployees.size(); ++i) {
            assertNotNull(listOfEmployees.get(i).getId(), "Employee ID should not be null");
            assertEquals(employees.get(i).getId(), listOfEmployees.get(i).getId());
        }
    }
}
