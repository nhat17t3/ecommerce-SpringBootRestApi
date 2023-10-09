package com.nhat.demoSpringbooRestApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @EmbeddedId
    private UserProductKey id = new UserProductKey();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
//    @JoinColumn(name = "user_id")
    private User user;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("productId")
//    @JoinColumn(name = "product_id")
    private Product product;

    @NotBlank
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;




}
