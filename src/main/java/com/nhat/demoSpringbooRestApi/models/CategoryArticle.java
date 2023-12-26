package com.nhat.demoSpringbooRestApi.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "category-article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  int id;

    @Column(name = "name", length = 100, nullable = false)
    @NotNull(message = "{error.category-article.name.null}")
    @NotBlank(message = "{error.category-article.name.blank}")
    @Size(min = 1, max = 100, message = "{error.category-article.name.size")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "categoryArticle", cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();

}
