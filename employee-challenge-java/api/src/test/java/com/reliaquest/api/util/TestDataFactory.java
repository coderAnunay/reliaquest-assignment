package com.reliaquest.api.util;

import com.reliaquest.api.dto.CreateEmployeeDTO;
import com.reliaquest.api.dto.EmployeeDTO;
import java.util.*;
import java.util.stream.IntStream;
import net.datafaker.Faker;

/**
 * Utility class to generate reusable test data for unit and integration tests.
 */
public class TestDataFactory {

    private static final Faker faker = new Faker();

    private static final Map<String, Integer> employeeSalaryRange = Map.of("min", 20000, "max", 2000000);
    private static final Map<String, Integer> employeeAgeRange = Map.of("min", 16, "max", 76);

    public static EmployeeDTO getTestEmployeeDTO() {
        return new EmployeeDTO(
                UUID.randomUUID().toString(),
                faker.name().fullName(),
                faker.number().numberBetween(employeeSalaryRange.get("min"), employeeSalaryRange.get("max")),
                faker.number().numberBetween(employeeAgeRange.get("min"), employeeAgeRange.get("max")),
                faker.job().title(),
                faker.internet().emailAddress());
    }

    public static EmployeeDTO getTestEmployeeDTOFromCreateEmployeeDTO(CreateEmployeeDTO createEmployeeDTO) {
        return new EmployeeDTO(
                UUID.randomUUID().toString(),
                createEmployeeDTO.getName(),
                createEmployeeDTO.getSalary(),
                createEmployeeDTO.getAge(),
                createEmployeeDTO.getTitle(),
                faker.internet().emailAddress());
    }

    // To make integration tests deterministic (e.g search api), we pass a name
    public static CreateEmployeeDTO getTestCreateEmployeeDTO(final String name) {
        return new CreateEmployeeDTO(
                name,
                faker.number().numberBetween(employeeSalaryRange.get("min"), employeeSalaryRange.get("max")),
                faker.number().numberBetween(employeeAgeRange.get("min"), employeeAgeRange.get("max")),
                faker.job().title());
    }

    public static List<EmployeeDTO> getTestEmployeeDTOList(int count) {
        return IntStream.range(0, count).mapToObj(i -> getTestEmployeeDTO()).toList();
    }

    public static List<EmployeeDTO> getTestEmployeeDTOList(List<String> names) {
        return IntStream.range(0, names.size())
                .mapToObj(i -> {
                    String name = names.get(i);
                    return new EmployeeDTO(
                            UUID.randomUUID().toString(),
                            name,
                            faker.number()
                                    .numberBetween(employeeSalaryRange.get("min"), employeeSalaryRange.get("max")),
                            faker.number().numberBetween(employeeAgeRange.get("min"), employeeAgeRange.get("max")),
                            faker.job().title(),
                            faker.internet().emailAddress());
                })
                .toList();
    }

    public static int getHighestSalary(List<EmployeeDTO> employees) {
        return Optional.ofNullable(employees).orElse(List.of()).stream()
                .filter(e -> e.getEmployeeSalary() != null)
                .map(EmployeeDTO::getEmployeeSalary)
                .max(Integer::compareTo)
                .orElse(0);
    }

    public static List<String> getTopTenHighestEarningEmployeeNames(List<EmployeeDTO> employees) {
        return Optional.ofNullable(employees).orElse(List.of()).stream()
                .filter(e -> e.getEmployeeSalary() != null && e.getEmployeeName() != null)
                .sorted(Comparator.comparing(EmployeeDTO::getEmployeeSalary).reversed())
                .limit(10)
                .map(EmployeeDTO::getEmployeeName)
                .toList();
    }
}
