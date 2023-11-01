package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.ProductListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.ProductRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.Category;
import com.nhat.demoSpringbooRestApi.models.Image;
import com.nhat.demoSpringbooRestApi.models.Product;
import com.nhat.demoSpringbooRestApi.repositories.ImageRepo;
import com.nhat.demoSpringbooRestApi.repositories.ProductRepo;
import com.nhat.demoSpringbooRestApi.services.CategoryService;
import com.nhat.demoSpringbooRestApi.services.ProductService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    protected ImageRepo imageRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    MinIOService minIOService;

    @Override
    public ProductListResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepo.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();

//        List<ProductRequestDTO> productRequestDTO = products.stream().map(product -> modelMapper.map(product, ProductRequestDTO.class))
//                .collect(Collectors.toList());

        ProductListResponseDTO productResponse = new ProductListResponseDTO();

        productResponse.setContent(products);
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

        productResponse.setContent(products);
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

        productResponse.setContent(products);
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
    public Product createProduct(ProductRequestDTO productRequestDTO , MultipartFile imagePrimary, MultipartFile[] moreImages) {
        Category category = categoryService.findCategoryById(productRequestDTO.getCategoryId());

        Product product;
        product = modelMapper.map(productRequestDTO, Product.class);
        product.setCategory(category);
        Product newProduct =  productRepo.save(product);

        String imagePrimaryUrl = minIOService.uploadFile(imagePrimary);
        Image image1 = new Image();
        image1.setImagePath(imagePrimaryUrl);
        image1.setIsPrimary(true);
        image1.setProduct(product);
        imageRepo.save(image1);
        for (MultipartFile moreImage : moreImages) {
            System.out.println("Uploading image: " + moreImage.getOriginalFilename());  // In ra tên ảnh
            String imageUrl = minIOService.uploadFile(moreImage);
            Image image = new Image();
            image.setImagePath(imageUrl);
            image.setProduct(product);
            imageRepo.save(image);
        }

        Product existingProduct = getProductById(newProduct.getId());

        return getProductById(newProduct.getId());
    }

    @Override
    public Product updateProduct(Integer productId, ProductRequestDTO productRequestDTO , MultipartFile imagePrimary, MultipartFile[] moreImages) {
        Product existingProduct = getProductById(productId);
        Category category = categoryService.findCategoryById(productRequestDTO.getCategoryId());
        existingProduct.setCategory(category);
        existingProduct.setName(productRequestDTO.getName());
        existingProduct.setPrice(productRequestDTO.getPrice());
        existingProduct.setDescription(productRequestDTO.getDescription());

        List<Image> images = new ArrayList<>();
        // Nếu có ảnh chính mới, cập nhật
        if (imagePrimary != null) {
            imageRepo.delete(imageRepo.findByProductIdAndIsPrimary(productId,true));
            String imagePrimaryUrl = minIOService.uploadFile(imagePrimary);
            Image image1 = new Image();
            image1.setImagePath(imagePrimaryUrl);
            image1.setIsPrimary(true);
            imageRepo.save(image1);
        }

        // Nếu có thêm ảnh mới, cập nhật
        if (moreImages != null && moreImages.length > 0) {
            imageRepo.deleteAll(imageRepo.findByProductId(productId));
            List<String> moreImagesUrls = new ArrayList<>();

            for (MultipartFile moreImage : moreImages) {
                System.out.println("Uploading image: " + moreImage.getOriginalFilename());
                String imageUrl = minIOService.uploadFile(moreImage);
                Image image = new Image();
                image.setImagePath(imageUrl);
                imageRepo.save(image);
                images.add(image);
            }
            existingProduct.setImages(images);
        }
        return productRepo.save(existingProduct);
    }

    @Override
    public String deleteProduct(Integer productId) {
        Product existingProduct = getProductById(productId);
        imageRepo.deleteAll(imageRepo.findByProductId(productId));
        productRepo.delete(existingProduct);
        return "Product with productId: " + productId + " deleted successfully !!!";
    }

    @Override
    public byte[] exportReportPDF(String reportFormat) throws FileNotFoundException, JRException {
        String path = "C:\\Users\\\\ADMIN\\Desktop";
        List<Product> products = productRepo.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:product.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        //push data to report
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(products);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "Nhat");
        parameters.put("age", 24);
        parameters.put("datasetTable", dataSource);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //config FDF viewer
        if (reportFormat.equalsIgnoreCase("pdf")) {
//            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\product.pdf");
        JRPdfExporter jrPdfExporter = new JRPdfExporter();
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        configuration.setCompressed(true);
        jrPdfExporter.setConfiguration(configuration);
        jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        jrPdfExporter.exportReport();
        }
        //config FDF HTML
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\product.html");
        }
        //config FDF Excel
//        if (reportFormat.equalsIgnoreCase("xlsx")) {
//            JRXlsExporter exporter = new JRXlsExporter();
//            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
//            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
//            configuration.setOnePagePerSheet(true);
//            configuration.setDetectCellType(true);
//            exporter.setConfiguration(configuration);
//            exporter.exportReport();
//        }

//        return "Product report generated in path : " + path;
        return byteArrayOutputStream.toByteArray();
    }

}
