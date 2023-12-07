package com.nhat.demoSpringbooRestApi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

    @NotBlank(message = "{error.CategoryRequestDTO.name.blank}")
    @Size(max = 100, message = "{error.CategoryRequestDTO.name.size}")
    private String name;

//    private Integer parentId;

}
