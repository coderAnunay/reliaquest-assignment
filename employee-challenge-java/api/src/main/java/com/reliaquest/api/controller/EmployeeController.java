package com.reliaquest.api.controller;

import com.reliaquest.api.dto.CreateEmployeeDTO;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.exception.ClientBadRequestException;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController implements IEmployeeController<EmployeeDTO, CreateEmployeeDTO> {

    private final EmployeeService employeeService;

    public EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByNameSearch(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            throw new ClientBadRequestException("Search string must not be null or blank.");
        }
        List<EmployeeDTO> matches = employeeService.getEmployeesByNameSearch(searchString);
        return ResponseEntity.ok(matches);
    }

    @Override
    public ResponseEntity<EmployeeDTO> getEmployeeById(String id) {
        if (id == null || id.isBlank()) {
            throw new ClientBadRequestException("Employee ID is required");
        } else {
            try {
                UUID.fromString(id);
            } catch (IllegalArgumentException ex) {
                throw new ClientBadRequestException("Employee ID must be a valid UUID");
            }
        }

        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
        return ResponseEntity.ok(highestSalary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> names = employeeService.getTopTenHighestEarningEmployeeNames();
        return ResponseEntity.ok(names);
    }

    @Override
    public ResponseEntity<EmployeeDTO> createEmployee(CreateEmployeeDTO employeeInput) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return null;
    }
}
