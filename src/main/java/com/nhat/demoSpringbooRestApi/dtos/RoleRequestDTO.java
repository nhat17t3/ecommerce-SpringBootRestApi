package com.nhat.demoSpringbooRestApi.dtos;

import jakarta.validation.constraints.NotBlank;

public class RoleRequestDTO {

    @NotBlank(message = "Tên không được để trống")
    private String name;

    public RoleRequestDTO() {
    }

    public RoleRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
