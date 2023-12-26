package com.nhat.demoSpringbooRestApi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryArticleRequestDTO {

    @NotBlank(message = "{error.CategoryArticleRequestDTO.name.blank}")
    @Size(max = 100, message = "{error.CategoryArticleRequestDTO.name.size}")
    private String name;

}
