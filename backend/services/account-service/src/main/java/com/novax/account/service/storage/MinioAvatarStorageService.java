package com.novax.account.service.storage;

import com.novax.common.core.minio.MinioProperties;
import com.novax.common.core.exception.BusinessException;
import com.novax.common.core.result.ResultCode;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * MinIO 头像存储服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioAvatarStorageService {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    public String uploadAvatar(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "Avatar file is empty");
        }
        if (!StringUtils.hasText(properties.getBucket())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "MinIO bucket is not configured");
        }

        String objectName = buildObjectName(userId, file.getOriginalFilename());
        ensureBucketExists();

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(args);
        } catch (Exception ex) {
            log.error("Failed to upload avatar to MinIO", ex);
            throw new BusinessException(ResultCode.EXTERNAL_API_ERROR, "Failed to upload avatar");
        }

        return buildObjectUrl(objectName);
    }

    private void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(properties.getBucket()).build()
            );
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucket()).build());
            }
        } catch (Exception ex) {
            log.error("Failed to ensure MinIO bucket exists", ex);
            throw new BusinessException(ResultCode.EXTERNAL_API_ERROR, "MinIO bucket error");
        }
    }

    private String buildObjectName(Long userId, String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String suffix = StringUtils.hasText(extension) ? "." + extension : "";
        String prefix = StringUtils.hasText(properties.getAvatarPrefix())
                ? properties.getAvatarPrefix().replaceAll("^/+", "").replaceAll("/+$", "") + "/"
                : "";
        return prefix + userId + "/" + UUID.randomUUID().toString().replace("-", "") + suffix;
    }

    private String buildObjectUrl(String objectName) {
        String baseUrl = StringUtils.hasText(properties.getPublicEndpoint())
                ? properties.getPublicEndpoint()
                : properties.getEndpoint();
        String normalized = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return normalized + "/" + properties.getBucket() + "/" + objectName;
    }
}
