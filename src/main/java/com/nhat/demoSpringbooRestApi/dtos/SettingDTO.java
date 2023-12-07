package com.nhat.demoSpringbooRestApi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingDTO {
    @NotBlank
    private String key;

    @NotBlank
    private String value;
}