package com.intellectual.service;

import com.intellectual.domain.Dish;
import com.intellectual.repository.DishRepository;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {

    private final MinioClient minioClient;
    private final DishRepository dishRepository;

    @Value("${minio.bucket}")
    private String bucket;

    // ── object key helpers ───────────────────────────────────────────────────

    private String dataKey(Long dishId)        { return "dishes/" + dishId + "/photo"; }
    private String contentTypeKey(Long dishId) { return "dishes/" + dishId + "/content-type"; }

    // ── photo bytes (cached in Redis 30 min) ─────────────────────────────────

    @Cacheable(value = "photos", key = "#dishId")
    public byte[] getPhoto(Long dishId) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(dataKey(dishId)).build())) {
            return stream.readAllBytes();
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) return null;
            throw new RuntimeException("MinIO read error", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load photo for dish " + dishId, e);
        }
    }

    /** Content-type is stored as MinIO object metadata — tiny, not cached. */
    public String getContentType(Long dishId) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucket).object(dataKey(dishId)).build());
            return stat.contentType();
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) return null;
            throw new RuntimeException("MinIO stat error", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get content-type for dish " + dishId, e);
        }
    }

    @CacheEvict(value = "photos", key = "#dishId")
    @Transactional
    public void savePhoto(Long dishId, MultipartFile file) {
        try (InputStream stream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(dataKey(dishId))
                    .stream(stream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            dishRepository.findById(dishId).ifPresent(d -> { d.setHasPhoto(true); dishRepository.save(d); });
            log.info("Photo saved to MinIO for dish {}", dishId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload photo for dish " + dishId, e);
        }
    }

    @CacheEvict(value = "photos", key = "#dishId")
    @Transactional
    public void deletePhoto(Long dishId) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucket).object(dataKey(dishId)).build());
            dishRepository.findById(dishId).ifPresent(d -> { d.setHasPhoto(false); dishRepository.save(d); });
            log.info("Photo deleted from MinIO for dish {}", dishId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete photo for dish " + dishId, e);
        }
    }
}
