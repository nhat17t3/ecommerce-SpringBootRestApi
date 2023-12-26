package com.nhat.demoSpringbooRestApi.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequestDTO {

    private int id;

    @NotBlank
    @Size(min = 5)
    private String name;

    private String shortDescription;

    private String content;

    @NotNull
    @Min(value = 1)
    private Integer categoryArticleId;

    private MultipartFile imagePath;

}
