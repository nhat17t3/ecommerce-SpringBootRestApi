package com.nhat.demoSpringbooRestApi.dtos;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequestDTO {

    @NotBlank(message = "Tên không được để trống")
    private String name;

//    @NotBlank(message = "code không được để trống")
    private String code;

    public CategoryRequestDTO() {
    }

    public CategoryRequestDTO(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
