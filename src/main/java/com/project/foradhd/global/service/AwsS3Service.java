package com.project.foradhd.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.foradhd.global.image.web.enums.ImagePathPrefix;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AwsS3Service {

    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final List<String> ALLOWED_IMAGE_FILE_EXTENSION_LIST = Arrays.asList("jpg", "jpeg", "png", "gif");
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadImages(ImagePathPrefix imagePathPrefix, List<MultipartFile> images) {
        return images.stream()
                .map(image -> uploadImage(imagePathPrefix, image))
                .toList();
    }

    private String uploadImage(ImagePathPrefix imagePathPrefix, MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new RuntimeException();
        }
        validateImageFileExtension(image.getOriginalFilename());
        return uploadImageToS3(imagePathPrefix, image);
    }

    private void validateImageFileExtension(String imageFilename) {
        String fileExtension = getFileExtension(imageFilename);
        boolean isAllowedExtension = ALLOWED_IMAGE_FILE_EXTENSION_LIST.stream()
                .anyMatch(allowedImageFileExtension -> allowedImageFileExtension.equalsIgnoreCase(fileExtension));
        if (!isAllowedExtension) {
            throw new RuntimeException();
        }
    }

    private String uploadImageToS3(ImagePathPrefix imagePathPrefix, MultipartFile image) {
        String randomImageFilename = generateRandomImageFilename(image.getOriginalFilename());
        String imagePath = imagePathPrefix.getPath() + randomImageFilename;
        try (InputStream is = image.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, imagePath, is, metadata);
            amazonS3.putObject(putObjectRequest);
            return amazonS3.getUrl(bucket, imagePath).toString();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private String getFileExtension(String filename) {
        int fileExtensionIndex = filename.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        if (fileExtensionIndex == -1) {
            throw new RuntimeException();
        }
        return filename.substring(fileExtensionIndex + 1);
    }

    private String generateRandomImageFilename(String originalImageFilename) {
        String fileExtension = getFileExtension(originalImageFilename);
        return UUID.randomUUID().toString().replace("-", "")
                + FILE_EXTENSION_SEPARATOR + fileExtension;
    }
}
