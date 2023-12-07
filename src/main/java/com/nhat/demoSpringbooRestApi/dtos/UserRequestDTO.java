package com.nhat.demoSpringbooRestApi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
//    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotEmpty
    private Set<String> roles = new HashSet<>();
}
