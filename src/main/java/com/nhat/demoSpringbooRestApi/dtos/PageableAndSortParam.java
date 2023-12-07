package com.nhat.demoSpringbooRestApi.dtos;

import com.nhat.demoSpringbooRestApi.configs.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableAndSortParam {

    public Integer pageNumber = Integer.valueOf(AppConstants.PAGE_NUMBER);

    public Integer pageSize = Integer.valueOf(AppConstants.PAGE_SIZE);

    public String sortBy = AppConstants.SORT_PRODUCTS_BY;

    public String sortOrder = AppConstants.SORT_DIR;

}
