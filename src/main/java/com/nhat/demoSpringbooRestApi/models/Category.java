package com.nhat.demoSpringbooRestApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;


/**
 * Persistent class for entity stored in table "category"
 */

@Entity
@Table(name= "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name",length = 100,nullable = false)
    @NotNull(message = "{error.category.name.null}")
    @NotBlank(message = "{error.category.name.blank}")
    @Size(max = 100, message = "{error.category.name.size}")
    private String name;

//    @ManyToOne(optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(name="parent_id", referencedColumnName="id", nullable = true)
//    private Category parent;
//
//    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
//    @JoinColumn(name="parent_id")
//    private List<Category> children;


    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade =  CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(@NotNull(message = "{error.category.name.null}") String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
