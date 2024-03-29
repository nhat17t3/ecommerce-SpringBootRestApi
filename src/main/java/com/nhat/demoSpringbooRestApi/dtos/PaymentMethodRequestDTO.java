package com.nhat.demoSpringbooRestApi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nhat.demoSpringbooRestApi.models.Order;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequestDTO {

    @NotBlank
    @Size(min = 3, message = "PaymentMethod name must contain atleast 3 characters")
    private String name;

    private String description;

    private Boolean isEnable;

}
