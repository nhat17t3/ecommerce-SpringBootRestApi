package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.BaseResponse;
import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.ProductFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.ProductRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Product;
import com.nhat.demoSpringbooRestApi.services.ProductService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/list")
    public ResponseEntity<BaseResponse> getAllProducts(ProductFilterRequestDTO productFilterRequestDTO) {
        DataTableResponseDTO<Product> products = productService.getAllProducts(productFilterRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("product.success.getAll", products);
        return ResponseEntity.status(200).body(baseResponse);
    }

//    @GetMapping("")
//    public ResponseEntity<ProductListResponseDTO> getAllProducts(
//            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
//            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
//            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
//            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
//        ProductListResponseDTO productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
//        return new ResponseEntity<ProductListResponseDTO>(productResponse, HttpStatus.OK);
//    }

    @GetMapping("/{productId}")
    public ResponseEntity<BaseResponse> getProductById(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("product.success.getById", product);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> addProduct(ProductRequestDTO productRequestDTO) {
        Product savedProduct = productService.createProduct(productRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("product.success.create-product", savedProduct);
        return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
    }
//    @PostMapping()
//    public ResponseEntity<Product> addProduct(@RequestParam(name = "name") String name,
//                                              @RequestParam(name = "description") String description,
//                                              @RequestParam(name = "price") Float price,
//                                              @RequestParam(name = "categoryId") Integer categoryId,
//                                              @RequestParam(name = "imagePrimary") MultipartFile imagePrimary,
//                                              @RequestParam(name = "moreImages") MultipartFile[] moreImages) {
//        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
//        productRequestDTO.setName(name);
//        productRequestDTO.setPrice(price);
//        productRequestDTO.setDescription(description);
//        productRequestDTO.setCategoryId(categoryId);
//
//        Product savedProduct = productService.createProduct(productRequestDTO,imagePrimary,moreImages);
//        return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
//    }

    @PutMapping("/{productId}")
    public ResponseEntity<BaseResponse> updateProduct(@PathVariable Integer productId,
                                                      @ModelAttribute ProductRequestDTO productRequestDTO) {
        Product savedProduct = productService.updateProduct(productId, productRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("product.success.update-product", savedProduct);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<BaseResponse> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("product.success.delate-product");
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/report/{format}")
    public ResponseEntity generateReportPDF(@PathVariable String format) throws FileNotFoundException, JRException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
//        MediaType media = new MediaType("application/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        httpHeaders.setContentType(media);
        byte[] reportStream = productService.exportReportPDF(format);
        return new ResponseEntity<>(reportStream, httpHeaders, HttpStatus.OK);
    }


}
