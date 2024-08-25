package com.project.foradhd.global.service;

import com.project.foradhd.global.image.web.enums.ImagePathPrefix;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {

    List<String> uploadImages(ImagePathPrefix imagePathPrefix, List<MultipartFile> images);

    void deleteImages(List<String> imagePaths);
}
