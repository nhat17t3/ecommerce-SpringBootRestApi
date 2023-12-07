package com.nhat.demoSpringbooRestApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    @NotNull(message = "{error.product.name.null}")
    @NotBlank(message = "{error.product.name.blank}")
    @Size(max = 255, message = "{error.product.name.size}")
    private String name;

    @Column(name = "price")
    @NotNull(message = "{error.product.price.null}")
    private Float price;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "description")
    private String description;

    @Column(name = "inStock")
    private Integer inStock;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "product", cascade =  CascadeType.ALL,fetch = FetchType.EAGER )
    private List<Image> images ;

    @NotNull(message = "{error.product.category_id.null}")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id",nullable = false)
    private Category category;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade =  CascadeType.ALL , fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails;

}
