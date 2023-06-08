package com.nhat.demoSpringbooRestApi.dtos;

import com.nhat.demoSpringbooRestApi.models.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    private int id;

    @NotBlank
    @Size(min = 5, message = "name product asleast 5 character")
    private String name;

    @NotNull
    private  float price;

    private String description;

    private  String image;

    @NotNull
    private int categoryId;


}
