package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.services.impl.MinioAdapter;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.text.StringContent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/minio")
public class MinioStorageController {
    @Autowired
    MinioAdapter minioAdapter;

    @Autowired
    MinioClient minioClient;

    @Autowired
    private WebClient webClient;

    @GetMapping(path = "/buckets")
    public List<Bucket> listBuckets() {
        return minioAdapter.getAllBuckets();
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
        minioAdapter.uploadFile(files.getOriginalFilename(), files.getBytes());
        Map<String, String> result = new HashMap<>();
        result.put("key", files.getOriginalFilename());
        return result;
    }

    @GetMapping(path = "/download1")
    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "file") String file) throws IOException {
        byte[] data = minioAdapter.getFile(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                .body(resource);

    }

    @SneakyThrows
    @GetMapping(path = "/download")
    public ResponseEntity<ByteArrayResource> previewFile(@RequestParam(value = "file") String file) throws IOException {
//        byte[] data = minioAdapter.getFile(file);

        InputStream stream =
                minioClient.getObject(
                        GetObjectArgs.builder().bucket("test1").object(file).build());
        byte[] content = IOUtils.toByteArray(stream);

        ByteArrayResource resource = new ByteArrayResource(content);

        System.out.println(resource);

        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "application/zip")
//                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                .body(resource);

    }

    @SneakyThrows
    @GetMapping(path = "/listObject")
    public List<String> getListObject() {
        List<String> listObjectNames = new ArrayList<>();
        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket("test1").build());

        for (Result<Item> result : results) {
            Item item = result.get();
            System.out.println(item);
//            listObjectNames.add(item.objectName());
            listObjectNames.add(item.toString());
        }

        return listObjectNames;

    }





}
