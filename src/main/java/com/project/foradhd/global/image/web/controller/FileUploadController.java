package com.project.foradhd.global.image.web.controller;

import com.project.foradhd.global.image.web.dto.request.ImageDeleteRequest;
import com.project.foradhd.global.image.web.dto.request.ImageUploadRequest;
import com.project.foradhd.global.image.web.dto.response.ImageUploadResponse;
import com.project.foradhd.global.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/files/images")
@RestController
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<ImageUploadResponse> uploadImages(@RequestPart List<MultipartFile> imageFileList,
                                                            @RequestPart ImageUploadRequest request) {
        List<String> imagePathList = fileStorageService.uploadImages(request.getImagePathPrefix(), imageFileList);
        return ResponseEntity.ok(new ImageUploadResponse(imagePathList));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImages(@RequestBody ImageDeleteRequest request) {
        fileStorageService.deleteImages(request.getImagePathList());
        return ResponseEntity.noContent().build();
    }
}
