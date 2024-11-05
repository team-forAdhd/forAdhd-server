package com.project.foradhd.domain.board.web.dto.request;

import com.project.foradhd.domain.board.persistence.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostRequestDto {
    private List<String> images;
    private String title;
    private String content;
    private boolean anonymous;
    private Category category;
}
