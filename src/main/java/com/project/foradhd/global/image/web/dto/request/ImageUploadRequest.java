package com.project.foradhd.global.image.web.dto.request;

import com.project.foradhd.global.image.web.enums.ImagePathPrefix;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ImageUploadRequest {

    private List<MultipartFile> imageFileList;
    private ImagePathPrefix imagePathPrefix = ImagePathPrefix.DEFAULT_IMAGE;
}
