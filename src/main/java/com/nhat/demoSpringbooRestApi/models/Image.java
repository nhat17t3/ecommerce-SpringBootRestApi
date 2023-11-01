package com.nhat.demoSpringbooRestApi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "image")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "image_path",length = 65555)
    @NotBlank
    private String imagePath;

    @Column(name = "alt_text")
    private String altText;

    private Boolean isPrimary;

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Image image = (Image) o;
//        return id == image.id && Objects.equals(imagePath, image.imagePath) && Objects.equals(altText, image.altText) && Objects.equals(isPrimary, image.isPrimary) && Objects.equals(product, image.product);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, imagePath, altText, isPrimary, product);
//    }
}
