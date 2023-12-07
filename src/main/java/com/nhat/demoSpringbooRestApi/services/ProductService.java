package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.ProductFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.ProductRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Product;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface ProductService {

    DataTableResponseDTO<Product> getAllProducts(ProductFilterRequestDTO productFilterRequestDTO);
    Product getProductById(Integer productId);
    Product createProduct (ProductRequestDTO product );
    Product updateProduct(Integer productId , ProductRequestDTO productRequestDTO);
    void deleteProduct (Integer productId);
    byte[] exportReportPDF (String reportFormat) throws FileNotFoundException, JRException;

}
