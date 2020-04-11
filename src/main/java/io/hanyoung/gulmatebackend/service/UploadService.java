package io.hanyoung.gulmatebackend.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UploadService {

    // param
    // 1. bucket name
    // 2. path
    private static final String S3_OBJECT_URL_TEMPLATE = "https://%s.s3.ap-northeast-2.amazonaws.com/%s";
    private final AmazonS3 s3;

    public String save(MultipartFile file, Optional<Map<String, String>> optionalMetadata) throws IOException {
        String bucketName = BucketName.FAMILY_PROFILE.getBucketName();
        String fileName = String.format("%s-%s", UUID.randomUUID(), file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(meta -> {
            if(!meta.isEmpty()) {
                meta.forEach(metadata::addUserMetadata);
            }
        });
        if(file.isEmpty() && !Arrays.asList(
                ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_SVG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalArgumentException("Requested File is empty OR not image file");
        }
        try {
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            metadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, byteArrayInputStream, metadata);
            s3.putObject(putObjectRequest);
            return String.format(S3_OBJECT_URL_TEMPLATE, bucketName, fileName);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to store file to s3", e);
        }
    }

    public void delete(String key) {
        s3.deleteObject(BucketName.FAMILY_PROFILE.getBucketName(), key);
    }

}
