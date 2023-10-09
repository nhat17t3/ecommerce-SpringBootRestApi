package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.ProductListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.ProductRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Product;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

public interface ProductService {

    ProductListResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductListResponseDTO searchProductByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductListResponseDTO searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    Product getProductById(Integer productId);
    Product createProduct (ProductRequestDTO product);
    Product updateProduct(Integer productId, ProductRequestDTO product);
    String deleteProduct (Integer productId);
    byte[] exportReportPDF (String reportFormat) throws FileNotFoundException, JRException;

}
