package com.nhat.demoSpringbooRestApi.services.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.messages.Bucket;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Clock;
import java.util.List;

@Service
public class MinioAdapter {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.buckek.name}")
    String defaultBucketName;

    @Value("${minio.default.folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        System.out.println("test minio");
        try {
            System.out.println("test minio");
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    @SneakyThrows
    public void uploadFile(String name, byte[] content) {
//        File file = new File("D:\\lap trinh java\\demoSpringbooRestApi\\tmp " + name);
//        file.canWrite();
//        file.canRead();
//        try {
//            FileOutputStream iofs = new FileOutputStream(file);
//            iofs.write(content);
//            minioClient.putObject(defaultBucketName, defaultBaseFolder + name, file.getAbsolutePath());
////            minioClient.putObject(defaultBucketName, defaultBaseFolder, name);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }

        InputStream inputStream = new ByteArrayInputStream(content);
        minioClient.putObject(
                PutObjectArgs.builder().bucket("test1").object(name).stream(
                                inputStream, -1,5242880 )
                        .build());


    }

    @SneakyThrows
    public byte[] getFile(String key) {
//        try {
//            InputStream obj = minioClient.getObject(defaultBucketName, defaultBaseFolder + "/" + key);
//
//            byte[] content = IOUtils.toByteArray(obj);
//            obj.close();
//            return content;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @PostConstruct
    public void init() {
    }
}
