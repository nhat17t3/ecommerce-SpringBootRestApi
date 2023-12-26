package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.ProductFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.ProductRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.Category;
import com.nhat.demoSpringbooRestApi.models.Image;
import com.nhat.demoSpringbooRestApi.models.Product;
import com.nhat.demoSpringbooRestApi.repositories.ImageRepo;
import com.nhat.demoSpringbooRestApi.repositories.ProductRepo;
import com.nhat.demoSpringbooRestApi.services.CategoryService;
import com.nhat.demoSpringbooRestApi.services.ProductService;
import com.nhat.demoSpringbooRestApi.specifications.ProductSpecifications;
import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    protected ImageRepo imageRepo;
    @Autowired
    MinIOService minIOService;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DataTableResponseDTO<Product> getAllProducts(ProductFilterRequestDTO productFilterRequestDTO) {
        Sort sortByAndOrder = productFilterRequestDTO.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(productFilterRequestDTO.getSortBy()).ascending()
                : Sort.by(productFilterRequestDTO.getSortBy()).descending();
        Pageable pageDetails = PageRequest.of(productFilterRequestDTO.getPageNumber(), productFilterRequestDTO.getPageSize(), sortByAndOrder);

        Specification<Product> spec = ProductSpecifications.searchByCondition(productFilterRequestDTO);

        Page<Product> pageProducts = productRepo.findAll(spec, pageDetails);

        List<Product> products = pageProducts.getContent();

//        List<ProductRequestDTO> productRequestDTO = products.stream().map(product -> modelMapper.map(product, ProductRequestDTO.class))
//                .collect(Collectors.toList());

        DataTableResponseDTO<Product> productResponse = new DataTableResponseDTO<>();

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
    public Product createProduct(ProductRequestDTO productRequestDTO) {
        //save product
        Category category = categoryService.findCategoryById(productRequestDTO.getCategoryId());
        Product product;
        product = modelMapper.map(productRequestDTO, Product.class);
        product.setCategory(category);
        Product newProduct = productRepo.save(product);

        //save Image
        String imagePrimaryUrl = minIOService.uploadFile(productRequestDTO.getImagePrimary());
        Image image1 = new Image();
        image1.setImagePath(imagePrimaryUrl);
        image1.setIsPrimary(true);
        image1.setProduct(product);
        imageRepo.save(image1);
        for (MultipartFile moreImage : productRequestDTO.getMoreImages()) {
            System.out.println("Uploading image: " + moreImage.getOriginalFilename());  // In ra tên ảnh
            String imageUrl = minIOService.uploadFile(moreImage);
            Image image = new Image();
            image.setImagePath(imageUrl);
            image.setProduct(product);
            imageRepo.save(image);
        }

        return getProductById(newProduct.getId());
    }

    @Override
    public Product updateProduct(Integer productId, ProductRequestDTO productRequestDTO) {
        Product existingProduct = getProductById(productId);
        Category category = categoryService.findCategoryById(productRequestDTO.getCategoryId());
        existingProduct.setCategory(category);
        existingProduct.setName(productRequestDTO.getName());
        existingProduct.setPrice(productRequestDTO.getPrice());
        existingProduct.setDescription(productRequestDTO.getDescription());

        List<Image> images = new ArrayList<>();
        // Nếu có ảnh chính mới, cập nhật
        if (productRequestDTO.getImagePrimary() != null) {
//            imageRepo.deleteByProductIdAndIsPrimary(productId);
            imageRepo.deleteAll(imageRepo.findByProductIdAndIsPrimary(productId, true));
            String imagePrimaryUrl = minIOService.uploadFile(productRequestDTO.getImagePrimary());
            Image image1 = new Image();
            image1.setImagePath(imagePrimaryUrl);
            image1.setIsPrimary(true);
            image1.setProduct(existingProduct);
            imageRepo.save(image1);
        }

        // Nếu có thêm ảnh mới, cập nhật
        MultipartFile[] moreImages = productRequestDTO.getMoreImages();
        if (moreImages != null && moreImages.length > 0) {
//            imageRepo.deleteByProductIdAndIsNotPrimary(productId);
            imageRepo.deleteAll(imageRepo.findByProductIdAndIsPrimary(productId, null));
            for (MultipartFile moreImage : moreImages) {
                System.out.println("Uploading image: " + moreImage.getOriginalFilename());
                String imageUrl = minIOService.uploadFile(moreImage);
                Image image = new Image();
                image.setImagePath(imageUrl);
                image.setProduct(existingProduct);
                imageRepo.save(image);
            }
        }
        return productRepo.save(existingProduct);
    }

    @Override
    public void deleteProduct(Integer productId) {
        Product existingProduct = getProductById(productId);
        imageRepo.deleteAll(imageRepo.findByProductId(productId));
        productRepo.delete(existingProduct);
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
