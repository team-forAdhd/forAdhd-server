package com.project.foradhd.domain.board.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class PostScrapDto {
    private String postScrapId;
    private String userId;
    private String postId;
    private LocalDateTime createdAt;
}
