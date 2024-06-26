package com.project.foradhd.global.image.web.dto.request;

import com.project.foradhd.global.image.web.enums.ImagePathPrefix;
import lombok.Getter;

@Getter
public class ImageUploadRequest {

    ImagePathPrefix imagePathPrefix = ImagePathPrefix.DEFAULT_IMAGE;
}
