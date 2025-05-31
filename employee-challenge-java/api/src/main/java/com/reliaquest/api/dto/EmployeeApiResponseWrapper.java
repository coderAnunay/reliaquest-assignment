package com.reliaquest.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A generic wrapper for API responses from upstream mock-employee-api.
 * @param <T> can typically be a list of EmployeeDTOs or a single EmployeeDTO.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeApiResponseWrapper<T> {

    private T data;

    private String status;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
