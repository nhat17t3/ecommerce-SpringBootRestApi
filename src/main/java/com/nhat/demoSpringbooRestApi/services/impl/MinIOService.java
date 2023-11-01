package com.nhat.demoSpringbooRestApi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class MinIOService {
    @Autowired
    MinioAdapter minioAdapter;

    public String uploadFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileUrl = "";
        try {
            // Check if the bucket exists
            boolean isExist = minioAdapter.bucketExists("test1");
            if (!isExist) {
                minioAdapter.makeBucket("test1");
            }
            // Upload the file
            String contentType = file.getContentType();
            minioAdapter.putObject("test1", file.getInputStream(),fileName,contentType);

            fileUrl = minioAdapter.getObjectUrl("test1", fileName , 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }
}
