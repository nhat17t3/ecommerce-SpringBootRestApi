package com.nhat.demoSpringbooRestApi.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    @NotBlank
    @Size(min = 5, message = "Role name must contain atleast 5 characters")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions", cascade = CascadeType.ALL)
    private Set<Role> roles = new HashSet<>();


}
