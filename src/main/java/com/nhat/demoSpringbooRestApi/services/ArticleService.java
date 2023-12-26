package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.ArticleFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.ArticleRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Article;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

public interface ArticleService {

    DataTableResponseDTO<Article> getAllArticles(ArticleFilterRequestDTO articleFilterRequestDTO);
    Article getArticleById(Integer articleId);
    Article createArticle (ArticleRequestDTO article );
    Article updateArticle(Integer articleId , ArticleRequestDTO articleRequestDTO);
    void deleteArticle (Integer articleId);

}
