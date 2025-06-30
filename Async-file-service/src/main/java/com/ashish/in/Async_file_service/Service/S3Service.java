package com.ashish.in.Async_file_service.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    private final S3Client s3Client;

    public void uploadFileToS3(MultipartFile file, String key) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    public InputStream downloadFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        System.out.printf("⬇️ Downloading from bucket='%s', key='%s', length=%d\n",
                bucketName, key, key.length());

        try {
            System.out.printf("🔍 Downloading from bucket=%s, key=%s\n", bucketName, key);
            return s3Client.getObject(getObjectRequest);
        } catch (software.amazon.awssdk.services.s3.model.S3Exception e) {
            System.err.printf("S3Exception: code=%s, message=%s\n", e.awsErrorDetails().errorCode(), e.getMessage());
            throw e; // Or wrap in RuntimeException
        }    }
}
