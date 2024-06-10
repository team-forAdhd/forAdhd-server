package com.project.foradhd.global.image.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageUploadResponse {

    private List<String> imagePathList;
}
