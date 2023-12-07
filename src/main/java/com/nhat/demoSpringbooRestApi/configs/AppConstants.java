package com.nhat.demoSpringbooRestApi.configs;

public class AppConstants {
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "2";
    public static final String SORT_CATEGORIES_BY = "categoryId";
    public static final String SORT_PRODUCTS_BY = "id";
    public static final String SORT_USERS_BY = "id";
    public static final String SORT_ORDERS_BY = "totalAmount";
    public static final String SORT_DIR = "asc";
    public static final Long ADMIN_ID = 101L;
    public static final Long USER_ID = 102L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String[] PUBLIC_URLS = { "/v3/api-docs/**", "/swagger-ui/**",
            "/register/**", "/login","/**" };

//    public static final String[] PUBLIC_URLS = { "/v3/api-docs/**", "/swagger-ui/**",
//            "/register/**", "/login","/roles/**","/products/**","/users/**","/comments/**" };
//    public static final String[] USER_URLS = { "/api/public/**","/categories/**", };
    public static final String[] USER_URLS = { };
    public static final String[] ADMIN_URLS = { "/api/admin/**"  };
}
