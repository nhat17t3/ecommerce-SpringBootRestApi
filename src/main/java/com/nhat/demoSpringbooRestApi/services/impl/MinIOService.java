package com.nhat.demoSpringbooRestApi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
public class MinIOService {
    @Autowired
    MinioAdapter minioAdapter;

    public String uploadFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String newFileName = String.format("%s_%s", timeStamp,fileName);
        String fileUrl = "";
        try {
            // Check if the bucket exists
            boolean isExist = minioAdapter.bucketExists("test1");
            if (!isExist) {
                minioAdapter.makeBucket("test1");
            }
            // Upload the file
            String contentType = file.getContentType();
            minioAdapter.putObject("test1", file.getInputStream(),newFileName,contentType);

//            fileUrl = minioAdapter.getObjectUrl("test1", newFileName , 300);
            fileUrl = "test1" + newFileName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }
}
