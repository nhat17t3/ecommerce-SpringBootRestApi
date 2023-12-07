//package com.nhat.demoSpringbooRestApi.controllers;
//
//import com.nhat.demoSpringbooRestApi.services.impl.MinioAdapter;
//import io.minio.*;
//import io.minio.errors.*;
//import io.minio.messages.Bucket;
//import io.minio.messages.Item;
//import lombok.SneakyThrows;
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import javax.swing.text.StringContent;
//import java.io.ByteArrayInputStream;
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.*;
//
//
//@RestController
//@RequestMapping("/minio")
//public class MinioStorageController {
//    @Autowired
//    MinioAdapter minioAdapter;
//
//    @Autowired
//    MinioClient minioClient;
//
//    @Autowired
//    private WebClient webClient;
//
//    @GetMapping(path = "/list-bucketName")
//    public List<String> listBuckets() {
//        return minioAdapter.listBucketNames();
//    }
//
//    @SneakyThrows
//    @GetMapping(path = "/list-Objects")
//    public List<String> getListObject() {
//        Iterator<Result<Item>> iterator =  minioAdapter.listObjects("test1").iterator();;
//        List<String> listFileName = new ArrayList<>();
//        while (iterator.hasNext()) {
//            Item item;
//            try {
//                item = iterator.next().get();
//            } catch (Exception e) {
//                continue;
//            }
//            String fileName = item.objectName();
//            listFileName.add(fileName);
//        }
//        return listFileName;
//    }
//
//    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public Map<String, String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
//        try {
//            minioAdapter.putObject("test1" , files.getInputStream(),"path2/" + files.getOriginalFilename() , files.getContentType());
//        } catch (ServerException e) {
//            throw new RuntimeException(e);
//        } catch (InsufficientDataException e) {
//            throw new RuntimeException(e);
//        } catch (ErrorResponseException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeyException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidResponseException e) {
//            throw new RuntimeException(e);
//        } catch (XmlParserException e) {
//            throw new RuntimeException(e);
//        } catch (InternalException e) {
//            throw new RuntimeException(e);
//        }
//        Map<String, String> result = new HashMap<>();
//        result.put("key", files.getOriginalFilename());
//        return result;
//    }
//
//    @GetMapping(path = "/download")
//    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "file") String file) throws IOException {
//        InputStream inputStream;
//        try {
//            inputStream = minioAdapter.getObject("test1", file);
//        } catch (ServerException e) {
//            throw new RuntimeException(e);
//        } catch (InsufficientDataException e) {
//            throw new RuntimeException(e);
//        } catch (ErrorResponseException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeyException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidResponseException e) {
//            throw new RuntimeException(e);
//        } catch (XmlParserException e) {
//            throw new RuntimeException(e);
//        } catch (InternalException e) {
//            throw new RuntimeException(e);
//        }
//        byte[] byteArray = IOUtils.toByteArray(inputStream);
//        ByteArrayResource resource = new ByteArrayResource(byteArray);
//
//        return ResponseEntity
//                .ok()
//                .contentLength(byteArray.length)
//                .header("Content-type", "application/octet-stream")
//                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
//                .body(resource);
//    }
//
//}
