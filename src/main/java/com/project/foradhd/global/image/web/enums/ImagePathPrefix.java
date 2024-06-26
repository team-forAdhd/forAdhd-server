package com.project.foradhd.global.image.web.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImagePathPrefix {

    DEFAULT_IMAGE("image/");

    private final String path;
}
