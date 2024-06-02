package com.project.foradhd.global.image.web.controller;

import com.project.foradhd.global.image.web.dto.request.ImageDeleteRequest;
import com.project.foradhd.global.image.web.dto.request.ImageUploadRequest;
import com.project.foradhd.global.image.web.dto.response.ImageUploadResponse;
import com.project.foradhd.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@RestController
public class ImageUploadController {

    private final AwsS3Service awsS3Service;

    @PostMapping
    public ResponseEntity<ImageUploadResponse> uploadImages(@RequestPart List<MultipartFile> imageFileList,
                                                            @RequestPart ImageUploadRequest request) {
        List<String> imagePathList = awsS3Service.uploadImages(request.getImagePathPrefix(), imageFileList);
        return ResponseEntity.ok(new ImageUploadResponse(imagePathList));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImages(@RequestBody ImageDeleteRequest request) {
        awsS3Service.deleteImages(request.getImagePathList());
        return ResponseEntity.noContent().build();
    }
}
