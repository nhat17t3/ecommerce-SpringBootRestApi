package com.nhat.demoSpringbooRestApi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {

    @NotNull
    private int userId;

    @NotNull
    private int productId;

    @NotBlank
    private String content;
}
