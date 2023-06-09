package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.ProductListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.ProductRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.Category;
import com.nhat.demoSpringbooRestApi.models.Product;
import com.nhat.demoSpringbooRestApi.repositories.ProductRepo;
import com.nhat.demoSpringbooRestApi.services.CategoryService;
import com.nhat.demoSpringbooRestApi.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductListResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepo.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();

        List<ProductRequestDTO> productRequestDTO = products.stream().map(product -> modelMapper.map(product, ProductRequestDTO.class))
                .collect(Collectors.toList());

        ProductListResponseDTO productResponse = new ProductListResponseDTO();

        productResponse.setContent(productRequestDTO);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductListResponseDTO searchProductByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepo.findByCategoryId(categoryId, pageDetails);

        List<Product> products = pageProducts.getContent();

//        if (products.size() == 0) {
//            throw new CustomException("Products not found with category ID: " + categoryId);
//        }

        List<ProductRequestDTO> productRequestDTO = products.stream().map(product -> modelMapper.map(product, ProductRequestDTO.class))
                .collect(Collectors.toList());

        ProductListResponseDTO productResponse = new ProductListResponseDTO();

        productResponse.setContent(productRequestDTO);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductListResponseDTO searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepo.findByNameLike(keyword, pageDetails);

        List<Product> products = pageProducts.getContent();

//        if (products.size() == 0) {
//            throw new CustomException("Products not found with keyword: " + keyword);
//        }

        List<ProductRequestDTO> productRequestDTO = products.stream().map(product -> modelMapper.map(product, ProductRequestDTO.class))
                .collect(Collectors.toList());

        ProductListResponseDTO productResponse = new ProductListResponseDTO();

        productResponse.setContent(productRequestDTO);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public Product getProductById(Integer productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    @Override
    public Product createProduct(ProductRequestDTO productRequestDTO) {
        Category category = categoryService.findCategoryById(productRequestDTO.getCategoryId());

        Product product = modelMapper.map(productRequestDTO, Product.class);
        product.setCategory(category);

        return productRepo.save(product);
    }

    @Override
    public Product updateProduct(Integer productId, ProductRequestDTO productRequestDTO) {
        Product product = getProductById(productId);
        Category category = categoryService.findCategoryById(productRequestDTO.getCategoryId());

//        product = modelMapper.map(productRequestDTO, Product.class);
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());
        product.setImage(productRequestDTO.getImage());
        product.setCategory(category);
        return productRepo.save(product);
    }

    @Override
    public String deleteProduct(Integer productId) {
        Product existingProduct = getProductById(productId);
        productRepo.delete(existingProduct);
        return "Product with productId: " + productId + " deleted successfully !!!";

    }
}
