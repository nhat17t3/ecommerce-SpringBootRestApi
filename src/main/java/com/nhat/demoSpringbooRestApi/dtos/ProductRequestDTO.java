package com.nhat.demoSpringbooRestApi.dtos;

import com.nhat.demoSpringbooRestApi.models.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    private int id;

    @NotBlank
    @Size(min = 5)
    private String name;

    @NotNull
    private Float price;

    private String shortDescription;

    private String description;

    @NotNull
    @Min(value = 1)
    private Integer categoryId;

    private MultipartFile imagePrimary;

    private MultipartFile[] moreImages;

}
