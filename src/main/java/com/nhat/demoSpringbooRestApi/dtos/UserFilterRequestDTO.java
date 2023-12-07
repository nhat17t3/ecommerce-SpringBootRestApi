package com.nhat.demoSpringbooRestApi.dtos;

import com.nhat.demoSpringbooRestApi.configs.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterRequestDTO extends PageableAndSortParam {

    private String inputSearch;

}
