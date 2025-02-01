package com.project.foradhd.global.image.web.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ImageDeleteRequest {

    private List<String> imagePathList;
}
