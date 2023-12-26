package com.nhat.demoSpringbooRestApi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFilterRequestDTO extends PageableAndSortParam {

    private String inputSearch;

    private List<String> categoryArticleIds;
}
