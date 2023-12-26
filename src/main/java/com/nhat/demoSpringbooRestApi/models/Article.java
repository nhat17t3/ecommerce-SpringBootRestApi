package com.nhat.demoSpringbooRestApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    @NotNull(message = "{error.article.name.null}")
    @NotBlank(message = "{error.article.name.blank}")
    @Size(max = 255, message = "{error.article.name.size}")
    private String name;

    @Column(name = "short_description",length = 10000)
    private String shortDescription;

    @Column(name = "content", columnDefinition = "LONGBLOB")
    private String content;


    @Column(name = "imagePath")
    private String imagePath;

    @NotNull(message = "{error.article.categoryArticle_id.null}")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryArticle_id", referencedColumnName = "id",nullable = false)
    private CategoryArticle categoryArticle;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}
